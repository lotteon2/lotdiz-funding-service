//package com.lotdiz.fundingservice.dto.response;
//
//import com.lotdiz.fundingservice.entity.ProductFunding;
//import lombok.Builder;
//
//@Builder
//public class ProductFundingResponse {
//  private Long productId;
//  private Long productFundingPrice;
//  private Long productFundingQuantity;
//
//  public ProductFundingResponse fromEntity(ProductFunding productFunding) {
//    return ProductFundingResponse.builder()
//        .productId(productFunding.getProductId())
//        .productFundingPrice(productFunding.getProductFundingPrice())
//        .productFundingQuantity(productFunding.getProductFundingQuantity())
//        .build();
//  }
//}
