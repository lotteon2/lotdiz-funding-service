package com.lotdiz.fundingservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TotalAmountWithProjectIdDto {
  private Long projectId;
  private Long totalAmount;
}
