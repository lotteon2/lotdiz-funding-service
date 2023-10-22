package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.response.MemberInfoResponseDto;
import com.lotdiz.fundingservice.dto.response.SupportWithUsResponseDto;
import com.lotdiz.fundingservice.dto.response.SupporterInfoResponseDto;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.SupporterWithUs;
import com.lotdiz.fundingservice.exception.MemberServiceClientOutOfServiceException;
import com.lotdiz.fundingservice.repository.FundingRepository;
import com.lotdiz.fundingservice.repository.SupporterWithUsRepository;
import com.lotdiz.fundingservice.service.client.MemberServiceClient;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SupportWithUsService {
    private final FundingRepository fundingRepository;
    private final SupporterWithUsRepository supporterWithUsRepository;
    private final MemberServiceClient memberServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

@Transactional
public SupportWithUsResponseDto getSupportWithUsInfo(Long projectId, Pageable pageable){
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    Page<Funding> fundingPerPage = fundingRepository.findByProjectId(projectId, pageable);
    List<Funding> fundingList = fundingPerPage.toList();

    List<Long> fundingIds = fundingList.stream().map(Funding::getFundingId).collect(Collectors.toList());

    Long count = fundingRepository.countFundingByProjectId(projectId);

    Long totalPages = (long) fundingPerPage.getTotalPages();

    List<Long> memberIds = fundingList.stream().map(Funding::getMemberId).collect(Collectors.toList());

    // supporterWithUs 정보
    List<SupporterWithUs> supporterWithUsList = supporterWithUsRepository.findAllByFundingIdIsIn(fundingIds);
    List<Long> supporterWithUsIds = supporterWithUsList.stream().map(SupporterWithUs::getSupporterWithUsId).collect(
            Collectors.toList());
    List<Boolean> supporterWithUsIsNamePublicList = supporterWithUsList.stream().map(SupporterWithUs::getSupporterWithUsIsNamePublic).collect(
            Collectors.toList());
    List<Boolean> supporterWithUsIsAmountPublicList = supporterWithUsList.stream().map(SupporterWithUs::getSupporterWithUsIsAmountPublic).collect(
            Collectors.toList());

    // member-service로 정보 요청
    Map<String, MemberInfoResponseDto> memberInfos = (Map<String, MemberInfoResponseDto>)circuitBreaker.run(()->memberServiceClient.getMemberInfo(
            memberIds).getData(), throwable -> new MemberServiceClientOutOfServiceException());

    List<String> memberNames = memberIds.stream().map(Object::toString).map(memberInfos::get).filter(Objects::nonNull).map(MemberInfoResponseDto::getMemberName).collect(
            Collectors.toList());

    List<String> memberProfileImageUrls = memberIds.stream().map(Object::toString).map(memberInfos::get).filter(Objects::nonNull).map(MemberInfoResponseDto::getMemberProfileImageUrl).collect(
            Collectors.toList());

    // funding 정보
    List<Long> fundingTotalAmounts = fundingList.stream().map(Funding::getFundingTotalAmount).collect(
            Collectors.toList());
    List<Long> fundingSupportAmounts = fundingList.stream().map(Funding::getFundingSupportAmount).collect(
            Collectors.toList());
    List<LocalDateTime> createdAts = fundingList.stream().map(Funding::getCreatedAt).collect(
            Collectors.toList());

    List<SupporterInfoResponseDto> supporterInfoResponseDtos = new ArrayList<>();
    for(int i=0; i<supporterWithUsIds.size(); i++){
        SupporterInfoResponseDto supporterInfoResponseDto = SupporterInfoResponseDto.builder()
                .supportWithUsId(supporterWithUsIds.get(i))
                .supporterWithUsIsNamePublic(supporterWithUsIsNamePublicList.get(i))
                .supporterWithUsIsAmountPublic(supporterWithUsIsAmountPublicList.get(i))
                .memberId(memberIds.get(i))
                .memberName(memberNames.get(i))
                .memberProfileImageUrl(memberProfileImageUrls.get(i))
                .fundingtTotalAmount(fundingTotalAmounts.get(i))
                .fundingSupportAmount(fundingSupportAmounts.get(i))
                .createdAt(createdAts.get(i))
                .build();

        supporterInfoResponseDtos.add(supporterInfoResponseDto);
    }

    return SupportWithUsResponseDto.builder()
            .totalPages(totalPages)
            .count(count)
            .supporterInfoResponseDtos(supporterInfoResponseDtos)
            .build();
}


}
