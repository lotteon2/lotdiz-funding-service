package com.lotdiz.fundingservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupportWithUsResponseDto {
  private Long count;
  private List<SupporterInfoResponseDto> supporterInfoResponseDtos;
}
