package com.lotdiz.fundingservice.controller.restcontroller;

import com.lotdiz.fundingservice.dto.request.CreateFundingRequestDto;
import com.lotdiz.fundingservice.dto.response.FundingAndTotalPageResponseDto;
import com.lotdiz.fundingservice.dto.response.FundingInfoResponseDto;
import com.lotdiz.fundingservice.service.FundingService;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundingRestController {
  private final FundingService fundingService;

  @PostMapping("/projects/{projectId}/fundings")
  public ResponseEntity<SuccessResponse> createFunding(
      @RequestBody CreateFundingRequestDto createFundingRequestDto) {
    // Funding, ProductFunding 저장.
    fundingService.createFunding(createFundingRequestDto);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("펀딩 성공")
                .build());
  }

  @GetMapping("/fundings")
  public ResponseEntity<SuccessResponse<Map<String, FundingAndTotalPageResponseDto>>>
      getFundingsByMember(
          @RequestHeader(required = true) Long memberId,
          @PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Direction.DESC)
              Pageable pageable) {

    FundingAndTotalPageResponseDto responseDto =
        fundingService.getFundingInfoListResponse(memberId, pageable);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, FundingAndTotalPageResponseDto>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("펀딩 내역 조회 성공")
                .data(Map.of("fundingList", responseDto))
                .build());
  }
}
