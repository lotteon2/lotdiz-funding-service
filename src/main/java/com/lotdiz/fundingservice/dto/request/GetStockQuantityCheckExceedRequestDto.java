package com.lotdiz.fundingservice.dto.request;

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
public class GetStockQuantityCheckExceedRequestDto {
    private List<ProductStockCheckRequest> productStockCheckRequests;
}
