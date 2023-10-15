package com.lotdiz.fundingservice.service.client;

import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @FeignClient(name = "deliveryServiceClient", url = "${endpoint.delivery-service}")
@FeignClient(
    name = "deliveryServiceClient",
    url = "https://952a1112-3483-413d-90a6-6befa9974329.mock.pstmn.io")
public interface DeliveryServiceClient {
  @PostMapping("/delivery/get-status/")
  SuccessResponse<List<String>> getDeliveryStatus(@RequestBody List<Long> fundingIds);
}
