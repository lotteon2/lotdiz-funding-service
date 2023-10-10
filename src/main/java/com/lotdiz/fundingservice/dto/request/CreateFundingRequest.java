package com.lotdiz.fundingservice.dto.request;

import com.lotdiz.fundingservice.dto.common.ProductFundingDTO;
import com.lotdiz.fundingservice.entity.Funding;
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
public class CreateFundingRequest {
  private Long memberId;
  private Long projectId;
  private List<ProductFundingDTO> products;
  private String fundingSupporterEmail;
  private Long fundingTotalAmount;
  private Boolean fundingIsRefundable;
  private Long fundingSupportAmount;
  private Long fundingLotdealDiscountAmount;
  private Long fundingMembershipDiscountAmount;
  private Long fundingUsedPoint;
  private Boolean fundingPrivacyAgreement;
  private Long deliveryCost;
  private Long fundingPaymentsActualAmount;
  private String addressRecipientName;
  private String addressRecipientPhoneNumber;
  private String addressDetail;
  private String addressZipCode;
  private Boolean addressIsDefault;

  public Funding toFundingEntity() {
    // return FundingMapper.INSTANCE.createFundingRequestDtoToFunding(this);
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

  // TODO: 결제, 배송지 정보는 payment-service, delivery-service로 보내 toEntity() 처리되도록

  //  public ProductFunding toProductFundingEntity(){
  //    return FundingMapper
  //  }
}
