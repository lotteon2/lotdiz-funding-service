package com.lotdiz.fundingservice.controller.restcontroller;

import com.lotdiz.fundingservice.dto.request.CreateFundingRequestDto;
import com.lotdiz.fundingservice.dto.response.FundingInfoResponseDto;
import com.lotdiz.fundingservice.dto.response.ProjectAndProductInfoResponseDto;
import com.lotdiz.fundingservice.dto.response.SupportWithUsResponseDto;
import com.lotdiz.fundingservice.service.FundingService;
import com.lotdiz.fundingservice.service.SupportWithUsService;
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
import org.springframework.web.bind.annotation.PathVariable;
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
  private final SupportWithUsService supportWithUsService;

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
  public ResponseEntity<SuccessResponse<Map<String, List<FundingInfoResponseDto>>>>
      getFundingsByMember(
          @RequestHeader(required = false) Long memberId,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "createdAt") String sort) {

    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sort).descending());

    List<FundingInfoResponseDto> fundingInfoListResponse =
        fundingService.getFundingInfoListResponse(memberId, pageRequest);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, List<FundingInfoResponseDto>>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("펀딩 내역 조회 성공")
                .data(Map.of("fundingList", fundingInfoListResponse))
                .build());
  }

  @GetMapping("/fundings/{fundingId}")
  public ResponseEntity<SuccessResponse<Map<String, ProjectAndProductInfoResponseDto>>>
      getFundingDetails(@PathVariable Long fundingId) {

    ProjectAndProductInfoResponseDto projectAndProductInfoResponseDto =
        fundingService.getFundingDetailsResponse(fundingId);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, ProjectAndProductInfoResponseDto>>builder()
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
}
