package com.lotdiz.fundingservice.controller.client;

import com.lotdiz.fundingservice.dto.request.FundingAchievementResultOfProjectRequestDto;
import com.lotdiz.fundingservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.fundingservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.fundingservice.dto.response.FundingAchievementResultOfProjectResponseDto;
import com.lotdiz.fundingservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.fundingservice.service.ProjectClientService;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FundingClientController {

  private final ProjectClientService projectClientService;

  @PostMapping("/fundings/check-target-amount-exceed")
  public SuccessResponse<List<GetTargetAmountCheckExceedResponseDto>> getIsTargetAmountExceed(
      @Valid @RequestBody
          List<GetTargetAmountCheckExceedRequestDto> getTargetAmountCheckExceedRequestDtos) {
    List<GetTargetAmountCheckExceedResponseDto> getTargetAmountCheckExceedResponseDtos =
        projectClientService.getTargetAmountCheckExceed(getTargetAmountCheckExceedRequestDtos);

    return SuccessResponse.<List<GetTargetAmountCheckExceedResponseDto>>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("성공")
        .data(getTargetAmountCheckExceedResponseDtos)
        .build();
  }

  @PostMapping("/projects/achievement")
  public SuccessResponse<HashMap<String, FundingAchievementResultOfProjectResponseDto>>
      getFundingOfProject(@RequestBody List<FundingAchievementResultOfProjectRequestDto> projects) {

    HashMap<String, FundingAchievementResultOfProjectResponseDto> map =
        projectClientService.getFundingMultipleAchievementResults(projects);

    return SuccessResponse
        .<HashMap<String, FundingAchievementResultOfProjectResponseDto>>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("펀딩 달성 결과 조회 (목록 페이지) 성공")
        .data(map)
        .build();
  }

  @GetMapping("/projects/{projectId}/achievement")
  SuccessResponse<FundingAchievementResultOfProjectDetailResponseDto> getFundingOfProjectDetail(
      @PathVariable Long projectId, @RequestParam Long projectTargetAmount) {

    FundingAchievementResultOfProjectDetailResponseDto dto = projectClientService.getFundingSingleAchievementResult(projectId, projectTargetAmount);

    return SuccessResponse.<FundingAchievementResultOfProjectDetailResponseDto>builder()
            .code(String.valueOf(HttpStatus.OK.value()))
            .message(HttpStatus.OK.name())
            .detail("펀딩 달성 결과 조회 (상세 페이지) 성공")
            .data(dto)
            .build();
  }
}
