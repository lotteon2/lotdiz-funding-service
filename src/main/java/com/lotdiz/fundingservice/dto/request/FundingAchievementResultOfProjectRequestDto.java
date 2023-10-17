package com.lotdiz.fundingservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FundingAchievementResultOfProjectRequestDto {

  private Long projectId;
  private Long projectTargetAmount;
}
