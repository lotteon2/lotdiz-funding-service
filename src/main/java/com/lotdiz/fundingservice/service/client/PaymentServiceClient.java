package com.lotdiz.fundingservice.service.client;

import com.lotdiz.fundingservice.dto.request.KakaoPayApproveRequestDto;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentServiceClient", url = "${endpoint.payment-service}")
public interface PaymentServiceClient {
  @PostMapping("/funding/payments/ready")
  ResponseEntity<SuccessResponse> payApprove(@RequestBody KakaoPayApproveRequestDto readyRequestDto);
}
