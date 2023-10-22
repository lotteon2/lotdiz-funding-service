package com.lotdiz.fundingservice.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfoResponseDto {
  private Long fundingId;
  private Long fundingPaymentsActualAmount;
  private LocalDateTime createdAt;
}
