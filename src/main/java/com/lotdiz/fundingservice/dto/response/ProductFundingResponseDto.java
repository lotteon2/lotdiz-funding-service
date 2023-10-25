package com.lotdiz.fundingservice.dto.response;

import com.lotdiz.fundingservice.dto.request.ProductFundingRequestDto;
import com.lotdiz.fundingservice.entity.ProductFunding;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductFundingResponseDto {
  private Long productId;
  private Long productFundingPrice;
  private Long productFundingQuantity;

  public ProductFundingResponseDto fromEntity(ProductFunding productFunding) {
    return ProductFundingResponseDto.builder()
        .productId(productFunding.getProductId())
        .productFundingPrice(productFunding.getProductFundingPrice())
        .productFundingQuantity(productFunding.getProductFundingQuantity())
        .build();
  }
}
