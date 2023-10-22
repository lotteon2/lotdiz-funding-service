package com.lotdiz.fundingservice.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoPayApproveRequestDto {
  private Long fundingId;
  private String tid;
  private String pgToken;
  private String partnerOrderId;
  private String partnerUserId;
}
