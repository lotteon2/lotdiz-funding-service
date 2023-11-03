package com.lotdiz.fundingservice.service.client;

import com.lotdiz.fundingservice.dto.response.GetDeliveryResponseDto;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "deliveryServiceClient", url = "${endpoint.delivery-service}")
public interface DeliveryServiceClient {
  @PostMapping("/delivery/get-status/")
  SuccessResponse<List<String>> getDeliveryStatus(@RequestBody List<Long> fundingIds);

  @GetMapping("/fundings/{fundingId}/delivery")
  SuccessResponse<Map<String, GetDeliveryResponseDto>> getDeliveryDetail(
      @PathVariable("fundingId") Long fundingId);
}
