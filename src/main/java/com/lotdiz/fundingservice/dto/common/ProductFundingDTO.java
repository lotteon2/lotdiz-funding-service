package com.lotdiz.fundingservice.dto.common;

import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import lombok.Builder;

@Builder
public class ProductFundingDTO {
  private Long productId;
  private Long productFundingPrice;
  private Long productFundingQuantity;

  // productFundingId는 나중에 em.persist()할때 자동으로 생성되는건가?
  public ProductFunding toEntity(Funding funding) {
    return ProductFunding.builder()
        .funding(funding)
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
