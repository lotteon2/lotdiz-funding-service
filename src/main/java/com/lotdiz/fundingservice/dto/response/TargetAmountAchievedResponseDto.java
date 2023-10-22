package com.lotdiz.fundingservice.dto.response;

import com.lotdiz.fundingservice.dto.TargetAmountAchievedDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TargetAmountAchievedResponseDto {
  private List<TargetAmountAchievedDto> targetAmountAchievedDtos;
}
