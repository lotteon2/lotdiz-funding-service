package com.lotdiz.fundingservice.dto.request;

import com.lotdiz.fundingservice.dto.AchievedResultOfProjectDto;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AchievedResultOfProjectResponseDto {
  private Map<Long, AchievedResultOfProjectDto> fundingAchievementResultOfProjects;
}
