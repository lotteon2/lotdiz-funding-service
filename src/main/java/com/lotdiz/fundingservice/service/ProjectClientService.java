package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.request.FundingAchievementResultOfProjectRequestDto;
import com.lotdiz.fundingservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.fundingservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.fundingservice.dto.response.FundingAchievementResultOfProjectResponseDto;
import com.lotdiz.fundingservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.repository.FundingRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectClientService {

  private final FundingRepository fundingRepository;

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

  public HashMap<String, FundingAchievementResultOfProjectResponseDto>
      getFundingMultipleAchievementResults(
          List<FundingAchievementResultOfProjectRequestDto> projects) {

    List<Long> projectIds =
        projects.stream()
            .map(FundingAchievementResultOfProjectRequestDto::getProjectId)
            .collect(Collectors.toList());

    // 관련 프로젝트에 해당하는 펀딩 조회
    Map<Long, List<Funding>> projectIdToFunding =
        fundingRepository.findAllByProjectIdIsIn(projectIds).stream()
            .collect(Collectors.groupingBy(Funding::getProjectId));

    // 프로젝트id 별로 펀딩 객체를 조회하여 정보를 계산하고 반환된 List 값을 projectId를 key로 HashMap에 추가.

    HashMap<String, FundingAchievementResultOfProjectResponseDto> map = new HashMap<>();

    for (FundingAchievementResultOfProjectRequestDto project : projects) {
      Long projectId = project.getProjectId();
      List<Funding> fundings = projectIdToFunding.get(projectId);

      long totalAmounts =
          (fundings != null)
              ? fundings.stream().mapToLong(Funding::getFundingTotalAmount).sum()
              : 0L;

      FundingAchievementResultOfProjectResponseDto dto =
          FundingAchievementResultOfProjectResponseDto.builder()
              .fundingAchievementRate(
                  totalAmounts * 100 / (fundings != null ? project.getProjectTargetAmount() : 1))
              .accumulatedFundingAmount(totalAmounts)
              .build();

      map.put(projectId.toString(), dto);
    }
    return map;
  }

  public FundingAchievementResultOfProjectDetailResponseDto getFundingSingleAchievementResult(
      Long projectId, Long projectTargetAmount) {

    List<Funding> fundings = fundingRepository.findByProjectId(projectId);

    long count = fundings.size();
    long totalAmounts = fundings.stream().mapToLong(Funding::getFundingTotalAmount).sum();

    return FundingAchievementResultOfProjectDetailResponseDto.builder()
        .fundingAchievementRate(totalAmounts * 100 / projectTargetAmount)
        .accumulatedFundingAmount(totalAmounts)
        .numberOfBuyers(count)
        .build();
  }
}
