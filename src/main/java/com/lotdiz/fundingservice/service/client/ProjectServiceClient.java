package com.lotdiz.fundingservice.service.client;

import com.lotdiz.fundingservice.dto.request.GetStockQuantityCheckExceedRequestDto;
import com.lotdiz.fundingservice.dto.request.UpdateProductStockQuantityRequestDto;
import com.lotdiz.fundingservice.dto.response.GetStockQuantityCheckExceedResponseDto;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="projectServiceClient", url="${endpoint.project-service}")
public interface ProjectServiceClient {

    @PostMapping("/projects/{project-id}/check-stock-quantity-exceed")
    SuccessResponse<GetStockQuantityCheckExceedResponseDto> getStockQuantityCheckExceed(
            @PathVariable("project-id") Long projectId,
            @RequestBody GetStockQuantityCheckExceedRequestDto getStockQuantityCheckExceedRequestDto
    );

    @PostMapping("/projects/{project-id}/update-stock-quantity")
    SuccessResponse updateStockQuantity(
            @RequestBody UpdateProductStockQuantityRequestDto updateProductStockQuantityRequestDto
    );


}
