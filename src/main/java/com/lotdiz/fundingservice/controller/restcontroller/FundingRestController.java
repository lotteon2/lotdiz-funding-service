package com.lotdiz.fundingservice.controller.restcontroller;

import com.lotdiz.fundingservice.dto.request.CreateFundingRequest;
import com.lotdiz.fundingservice.dto.response.GetFundingDetailResponse;
import com.lotdiz.fundingservice.service.FundingService;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FundingRestController {
  private final FundingService fundingService;

  @PostMapping("/projects/{projectId}/fundings")
  public ResponseEntity<SuccessResponse> createFunding(
      @RequestBody CreateFundingRequest createFundingRequest) {
    // Funding, ProductFunding 저장.
    Long fundingId = fundingService.createFunding(createFundingRequest);

    GetFundingDetailResponse getFundingDetailResponse =
        fundingService.getFundingDetailResponse(fundingId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("펀딩 성공")
                .data(Map.of("fundingDetail", getFundingDetailResponse))
                .build());
  }
}
