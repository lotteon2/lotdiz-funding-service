package com.lotdiz.fundingservice.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectAndMakerInfoResponseDto {
  private Long projectId;
  private String projectName;
  private Long projectTargetAmount;
  private String projectStatus;
  private String projectImageUrl;
  private String makerName;
  private Long remainingDays;
}
