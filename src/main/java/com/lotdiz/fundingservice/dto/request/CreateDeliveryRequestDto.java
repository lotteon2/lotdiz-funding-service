package com.lotdiz.fundingservice.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDeliveryRequestDto {
  private Long fundingId;
  private String deliveryRecipientName;
  private String deliveryRecipientPhoneNumber;
  private String deliveryRecipientEmail;
  private String deliveryRoadName;
  private String deliveryAddressDetail;
  private String deliveryZipCode;
  private String deliveryRequest;
  private Long deliveryCost;
}
