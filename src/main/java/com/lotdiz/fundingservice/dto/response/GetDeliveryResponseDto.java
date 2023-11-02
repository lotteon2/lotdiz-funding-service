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
public class GetDeliveryResponseDto {
  private Long deliveryId;
  private String deliveryRecipientName;
  private String deliveryRecipientPhoneNumber;
  private String deliveryRecipientEmail;
  private String deliveryRoadName;
  private String deliveryAddressDetail;
  private String deliveryZipcode;
  private Long deliveryCost;
  private String deliveryStatus;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
