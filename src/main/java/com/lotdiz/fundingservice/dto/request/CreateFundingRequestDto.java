package com.lotdiz.fundingservice.dto.request;

import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.SupporterWithUs;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateFundingRequestDto {
  private String tid;
  private String pgToken;
  private String partnerOrderId;
  private String partnerUserId;
  private Long projectId;
  private List<ProductFundingRequestDto> products;
  private String itemName;
  private String fundingSupporterEmail;
  private Long fundingTotalAmount;
  private Boolean fundingIsRefundable;
  private Long fundingSupportAmount;
  private Long fundingLotdealDiscountAmount;
  private Long fundingMembershipDiscountAmount;
  private Long fundingUsedPoint;
  private Boolean fundingPrivacyAgreement;
  private Boolean supporterWithUsIsNamePublic;
  private Boolean supporterWithUsIsAmountPublic;
  private Long deliveryCost;
  private Long fundingPaymentsActualAmount;
  private String deliveryAddressRecipientName;
  private String deliveryAddressRecipientPhoneNumber;
  private String deliveryAddressRequest;
  private String deliveryAddressRoadName;
  private String deliveryAddressDetail;
  private String deliveryAddressZipCode;

  public Funding toFundingEntity(Long memberId) {
    return Funding.builder()
        .memberId(memberId)
        .projectId(projectId)
        .fundingSupporterEmail(fundingSupporterEmail)
        .fundingTotalAmount(fundingTotalAmount)
        .fundingIsRefundable(fundingIsRefundable)
        .fundingSupportAmount(fundingSupportAmount)
        .fundingLotdealDiscountAmount(fundingLotdealDiscountAmount)
        .fundingMembershipDiscountAmount(fundingMembershipDiscountAmount)
        .fundingUsedPoint(fundingUsedPoint)
        .fundingPrivacyAgreement(fundingPrivacyAgreement)
        .build();
  }

  public SupporterWithUs toSupporterWithUsEntity(Funding funding){
    return SupporterWithUs.builder()
            .funding(funding)
            .supporterWithUsIsNamePublic(supporterWithUsIsNamePublic)
            .supporterWithUsIsAmountPublic(supporterWithUsIsAmountPublic)
            .build();
  }

}
