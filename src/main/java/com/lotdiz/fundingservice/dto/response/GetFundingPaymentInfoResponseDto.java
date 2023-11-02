package com.lotdiz.fundingservice.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetFundingPaymentInfoResponseDto {
  private Long fundingId;
  private Long fundingPaymentsActualAmount;
  private LocalDateTime paymentDate;
}
