package com.lotdiz.fundingservice.dto.common;

import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class ProductFundingDTO {
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

  public static ProductFundingDTO fromEntity(ProductFunding productFunding) {
    return ProductFundingDTO.builder()
        .productId(productFunding.getProductId())
        .productFundingPrice(productFunding.getProductFundingPrice())
        .productFundingQuantity(productFunding.getProductFundingQuantity())
        .build();
  }
}
