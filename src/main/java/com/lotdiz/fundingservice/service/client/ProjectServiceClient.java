package com.lotdiz.fundingservice.service.client;

import com.lotdiz.fundingservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.fundingservice.dto.response.ProductStockCheckResponse;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @FeignClient(name="projectServiceClient", url="${endpoint.project-service}")
@FeignClient(
    name = "projectServiceClient",
    url = "https://952a1112-3483-413d-90a6-6befa9974329.mock.pstmn.io")
public interface ProjectServiceClient {

  @PostMapping("/projects/check-stock-quantity-exceed")
  SuccessResponse<List<ProductStockCheckResponse>> getStockQuantityCheckExceed(
      @RequestBody List<Long> productIds);

  @PostMapping("/projects/update-stock-quantity")
  SuccessResponse updateStockQuantity(
      @RequestBody List<ProductStockUpdateRequest> productStockUpdateRequests);
}
