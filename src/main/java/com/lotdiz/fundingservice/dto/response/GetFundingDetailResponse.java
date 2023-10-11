package com.lotdiz.fundingservice.dto.response;

import com.lotdiz.fundingservice.dto.common.ProductFundingDto;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetFundingDetailResponse {
  // TODO: project정보, member정보, delivery정보 필드 추가하기.
  private Long fundingId;
  private List<ProductFundingDto> productList;


  public static GetFundingDetailResponse fromEntity(
      Funding funding, List<ProductFunding> productFundings) {
    List<ProductFundingDto> products =
        productFundings.stream().map(ProductFundingDto::fromEntity).collect(Collectors.toList());

    return GetFundingDetailResponse.builder()
        .fundingId(funding.getFundingId())
        .productList(products)
        .build();
  }
}
