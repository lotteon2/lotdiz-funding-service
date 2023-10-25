package com.lotdiz.fundingservice.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class FundingAchievementResultOfProjectDetailResponseDto {

  private Long fundingAchievementRate;
  private Long accumulatedFundingAmount;
  private Long numberOfBuyers;
}
