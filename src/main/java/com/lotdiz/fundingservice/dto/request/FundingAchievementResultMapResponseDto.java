package com.lotdiz.fundingservice.dto.request;

import com.lotdiz.fundingservice.dto.FundingAchievementResultOfProjectResponseDto;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FundingAchievementResultMapResponseDto {
  private Map<Long, FundingAchievementResultOfProjectResponseDto>
      fundingAchievementResultOfProjects;
}
