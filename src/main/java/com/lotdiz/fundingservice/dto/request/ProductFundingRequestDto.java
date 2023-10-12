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

  public ProductFunding toEntity(Funding funding) {
    return ProductFunding.builder()
        .funding(funding)
        .productId(productId)
        .productFundingPrice(productFundingPrice)
        .productFundingQuantity(productFundingQuantity)
        .build();
  }

  public static ProductFundingRequestDto fromEntity(ProductFunding productFunding) {
    return ProductFundingRequestDto.builder()
        .productId(productFunding.getProductId())
        .productFundingPrice(productFunding.getProductFundingPrice())
        .productFundingQuantity(productFunding.getProductFundingQuantity())
        .build();
  }
}
