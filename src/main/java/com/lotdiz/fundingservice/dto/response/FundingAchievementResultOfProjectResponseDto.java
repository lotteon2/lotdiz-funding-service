package com.lotdiz.fundingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FundingAchievementResultOfProjectResponseDto {

  private Long fundingAchievementRate;
  private Long accumulatedFundingAmount;
}
