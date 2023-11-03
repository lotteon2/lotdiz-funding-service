package com.lotdiz.fundingservice.service.client;

import com.lotdiz.fundingservice.dto.request.KakaoPayApproveRequestDto;
import com.lotdiz.fundingservice.dto.response.FundingPaymentInfoResponse;
import com.lotdiz.fundingservice.dto.response.PaymentInfoResponseDto;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "paymentServiceClient", url = "${endpoint.payment-service}")
public interface PaymentServiceClient {
  @PostMapping("/payments/get-payment-info")
  SuccessResponse<List<PaymentInfoResponseDto>> getPaymentInfo(@RequestBody List<Long> fundingIds);

  @PostMapping("/funding/payments/approval")
  ResponseEntity<SuccessResponse> payApprove(
      @RequestBody KakaoPayApproveRequestDto approveRequestDto);

  @GetMapping("/funding/payments/info")
  SuccessResponse<FundingPaymentInfoResponse> getFundingPaymentInfo(@RequestParam Long fundingId);


}
