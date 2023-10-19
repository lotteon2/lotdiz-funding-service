package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.fundingservice.dto.request.MemberInformationOfFundingResponseDto;
import com.lotdiz.fundingservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.repository.FundingRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectClientService {
  private final FundingRepository fundingRepository;

  @Qualifier("fundingQueryRepository")
  private final FundingRepository fundingQueryRepository;

  public List<GetTargetAmountCheckExceedResponseDto> getTargetAmountCheckExceed(
      List<GetTargetAmountCheckExceedRequestDto> getTargetAmountCheckExceedRequestDtos) {
    List<Long> projectIds =
        getTargetAmountCheckExceedRequestDtos.stream()
            .map(GetTargetAmountCheckExceedRequestDto::getProjectId)
            .collect(Collectors.toList());
    // 관련 프로젝트에 해당하는 펀딩 조회
    Map<Long, List<Funding>> projectIdToFunding =
        fundingRepository.findAllByProjectIdIsIn(projectIds).stream()
            .collect(Collectors.groupingBy(Funding::getProjectId));

    // 펀딩 달성률 체크 후 반환
    return getTargetAmountCheckExceedRequestDtos.stream()
        .map(
            requestDto -> {
              List<Funding> fundings = projectIdToFunding.get(requestDto.getProjectId());
              long totalAmounts = fundings.stream().mapToLong(Funding::getFundingTotalAmount).sum();

              return GetTargetAmountCheckExceedResponseDto.builder()
                  .projectId(requestDto.getProjectId())
                  .projectName(requestDto.getProjectName())
                  .makerMemberId(requestDto.getMakerMemberId())
                  .isTargetAmountExceed(requestDto.getProjectTargetAmount() <= totalAmounts)
                  .memberIds(
                      fundings.stream().map(Funding::getMemberId).collect(Collectors.toList()))
                  .build();
            })
        .collect(Collectors.toList());
  }

  public MemberInformationOfFundingResponseDto getMemberFundingList(Long projectId) {
    return MemberInformationOfFundingResponseDto.builder()
        .memberFundingInformationDtos(fundingQueryRepository.findMemberFundingInfo(projectId))
        .build();
  }
}
