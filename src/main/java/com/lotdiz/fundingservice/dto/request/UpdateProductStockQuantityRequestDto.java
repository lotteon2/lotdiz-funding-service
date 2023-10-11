package com.lotdiz.fundingservice.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductStockQuantityRequestDto {
    List<ProductStockUpdateRequest> ProductStockUpdateRequest;
}
