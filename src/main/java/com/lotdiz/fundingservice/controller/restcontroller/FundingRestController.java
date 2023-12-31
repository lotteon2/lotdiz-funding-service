package com.lotdiz.fundingservice.controller.restcontroller;

import com.lotdiz.fundingservice.dto.request.CreateFundingRequestDto;
import com.lotdiz.fundingservice.dto.response.FundingAndTotalPageResponseDto;
import com.lotdiz.fundingservice.dto.response.FundingDetailsInfoResponseDto;
import com.lotdiz.fundingservice.dto.response.ResultDataResponse;
import com.lotdiz.fundingservice.dto.response.SupportWithUsResponseDto;
import com.lotdiz.fundingservice.service.FundingService;
import com.lotdiz.fundingservice.service.SupportWithUsService;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundingRestController {
  private final FundingService fundingService;
  private final SupportWithUsService supportWithUsService;

  @PostMapping("/projects/{projectId}/fundings")
  public ResponseEntity<ResultDataResponse<Long>> createFunding(
      @RequestHeader Long memberId,
      @RequestBody CreateFundingRequestDto createFundingRequestDto) throws IOException {
    // 펀딩Id 반환
    Long fundingId = fundingService.createFunding(createFundingRequestDto, memberId);

    return ResponseEntity.ok()
        .body(
            new ResultDataResponse<>(
            String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.name(), "카카오 결제 완료", fundingId));
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

  @GetMapping("/fundings/{fundingId}")
  public ResponseEntity<SuccessResponse<Map<String, FundingDetailsInfoResponseDto>>>
      getFundingDetails(@PathVariable Long fundingId) {

    FundingDetailsInfoResponseDto projectAndProductInfoResponseDto =
        fundingService.getFundingDetailsResponse(fundingId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, FundingDetailsInfoResponseDto>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("펀딩 내역 상세 조회 성공")
                .data(Map.of("fundingDetails", projectAndProductInfoResponseDto))
                .build());
  }

  @GetMapping("/projects/{projectId}/supporter-with-us")
  public ResponseEntity<SuccessResponse<Map<String, SupportWithUsResponseDto>>>
      getSupporterWithUsInfo(
          @PathVariable Long projectId,
          @PageableDefault(page=0, size=20, sort="createdAt", direction = Direction.DESC) Pageable pageable){

    SupportWithUsResponseDto supportWithUsResponseDto =
        supportWithUsService.getSupportWithUsInfo(projectId, pageable);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, SupportWithUsResponseDto>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("함께하는 서포터 조회 성공")
                .data(Map.of("supporterWithUsInfo", supportWithUsResponseDto))
                .build());
  }

  @DeleteMapping("/fundings/{fundingId}")
  public ResponseEntity<SuccessResponse> cancelFunding(@PathVariable Long fundingId) {
    fundingService.cancelFunding(fundingId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("펀딩 취소 성공")
                .build());
  }
}
