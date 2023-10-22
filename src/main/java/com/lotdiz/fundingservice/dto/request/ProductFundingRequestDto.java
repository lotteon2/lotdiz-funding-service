package com.lotdiz.fundingservice.dto.request;

import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductFundingRequestDto {
  private Long productId;
  private Long productFundingPrice;
  private Long productFundingQuantity;

  // ProductFunding Entity에 저장
  public ProductFunding toEntity(Funding funding) {
    return ProductFunding.builder()
        .funding(funding)
        .productId(productId)
        .productFundingPrice(productFundingPrice)
        .productFundingQuantity(productFundingQuantity)
        .build();
  }
}
