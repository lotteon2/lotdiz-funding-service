package com.lotdiz.fundingservice.service.client;

import com.lotdiz.fundingservice.dto.response.PaymentInfoResponseDto;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentServiceClient", url = "${endpoint.payment-service}")
public interface PaymentServiceClient {
  @PostMapping("/payments/get-payment-info")
  SuccessResponse<List<PaymentInfoResponseDto>> getPaymentInfo(@RequestBody List<Long> fundingIds);
}
