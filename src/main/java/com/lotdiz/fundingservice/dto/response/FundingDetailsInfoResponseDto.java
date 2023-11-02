package com.lotdiz.fundingservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FundingDetailsInfoResponseDto {

  private Long projectId;
  private String projectStatus;
  private String projectName;
  private String makerName;

  private Long fundingId;
  private LocalDateTime createdAt;
  private LocalDateTime endDate;
  private String fundingStatus;
  private Long fundingTotalAmount;
  private Long fundingUsedPoint;
  private Long fundingSupportAmount;

  private List<ProductFundingInfoResponseDto> products;

  private Long deliveryCost;
  private String deliveryRecipientName;
  private String deliveryRecipientPhoneNumber;
  private String deliveryRoadName;
  private String deliveryAddressDetail;
  private String deliveryZipcode;

}
