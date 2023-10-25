package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.response.FundingAchievementResultForMemberResponseDto;
import com.lotdiz.fundingservice.dto.response.ProjectAndMakerInfoResponseDto;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.exception.ProjectAndMakerInfoNotFoundException;
import com.lotdiz.fundingservice.repository.FundingRepository;
import com.lotdiz.fundingservice.service.client.ProjectServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberClientService {
  private final CircuitBreakerFactory circuitBreakerFactory;
  private final FundingRepository fundingRepository;
  private final ProjectServiceClient projectServiceClient;

  public List<FundingAchievementResultForMemberResponseDto> getFundingInfoForMemberService(List<Long> projectIds) {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    // project-service로 각 프로젝트의 목표금액 찾기
    List<ProjectAndMakerInfoResponseDto> projectAndMakerInfoResponseDtos =
        (List<ProjectAndMakerInfoResponseDto>)
            circuitBreaker.run(
                () -> projectServiceClient.getProjectAndMakerInfo(projectIds).getData(),
                throwable -> new ProjectAndMakerInfoNotFoundException());

    // 관련 프로젝트에 해당하는 펀딩 조회
    Map<Long, List<Funding>> projectIdToFunding =
        fundingRepository.findAllByProjectIdIsIn(projectIds).stream()
            .collect(Collectors.groupingBy(Funding::getProjectId));

    // 각 프로젝트 id에 해당하는 펀딩정보 계산
    List<FundingAchievementResultForMemberResponseDto> result = new ArrayList<>();
    for (ProjectAndMakerInfoResponseDto projectInfo : projectAndMakerInfoResponseDtos) {
      List<Funding> fundings = projectIdToFunding.get(projectInfo.getProjectId());

      long totalAmounts =
          (fundings != null)
              ? fundings.stream().mapToLong(Funding::getFundingTotalAmount).sum()
              : 0L;

      FundingAchievementResultForMemberResponseDto dto =
          FundingAchievementResultForMemberResponseDto.builder()
              .projectId(projectInfo.getProjectId())
              .fundingAchievementRate(
                  totalAmounts
                      * 100
                      / (fundings != null ? projectInfo.getProjectTargetAmount() : 1))
              .accumulatedFundingAmount(totalAmounts)
              .build();

      result.add(dto);
    }

    return result;
  }
}
