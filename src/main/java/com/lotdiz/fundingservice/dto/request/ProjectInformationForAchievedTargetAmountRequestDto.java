package com.lotdiz.fundingservice.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectInformationForAchievedTargetAmountRequestDto {

  private Long projectId;
  private String projectName;
  private Long projectTargetAmount;
}
