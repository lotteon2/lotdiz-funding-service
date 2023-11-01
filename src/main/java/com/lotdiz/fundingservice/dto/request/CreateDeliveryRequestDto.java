package com.lotdiz.fundingservice.dto.request;

import com.lotdiz.fundingservice.entity.Funding;
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

  public static CreateDeliveryRequestDto toDto(Funding savedFunding, CreateFundingRequestDto createFundingRequestDto){
    CreateDeliveryRequestDto createDeliveryRequestDto = CreateDeliveryRequestDto.builder()
              .fundingId(savedFunding.getFundingId())
            .deliveryRecipientName(createFundingRequestDto.getDeliveryAddressRecipientName())
            .deliveryRecipientPhoneNumber(
                    createFundingRequestDto.getDeliveryAddressRecipientPhoneNumber())
            .deliveryRoadName(createFundingRequestDto.getDeliveryAddressRoadName())
            .deliveryAddressDetail(createFundingRequestDto.getDeliveryAddressRequest())
            .deliveryZipCode(createFundingRequestDto.getDeliveryAddressZipCode())
            .deliveryRequest(createFundingRequestDto.getDeliveryAddressRequest())
            .deliveryCost(createFundingRequestDto.getDeliveryCost())
            .build();
      return createDeliveryRequestDto;
  }
}
