package com.lotdiz.fundingservice.controller.clientcontroller;

import com.lotdiz.fundingservice.dto.request.AchievedResultOfProjectResponseDto;
import com.lotdiz.fundingservice.dto.request.FundingAchievementResultMapResponseDto;
import com.lotdiz.fundingservice.dto.request.FundingAchievementResultOfProjectRequestDto;
import com.lotdiz.fundingservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.fundingservice.dto.request.MemberInformationOfFundingResponseDto;
import com.lotdiz.fundingservice.dto.request.ProjectAmountWithIdRequestDto;
import com.lotdiz.fundingservice.dto.request.ProjectInformationForAchievedTargetAmountRequestDto;
import com.lotdiz.fundingservice.dto.response.FundingAchievementResultOfProjectDetailResponseDto;
import com.lotdiz.fundingservice.dto.response.FundingAchievementResultOfProjectResponseDto;
import com.lotdiz.fundingservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.fundingservice.dto.response.TargetAmountAchievedResponseDto;
import com.lotdiz.fundingservice.service.ProjectClientService;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectClientController {

  private final ProjectClientService projectClientService;

  @PostMapping("/fundings/check-target-amount-exceed")
  public ResponseEntity<SuccessResponse<List<GetTargetAmountCheckExceedResponseDto>>>
      getIsTargetAmountExceed(
          @Valid @RequestBody
              List<GetTargetAmountCheckExceedRequestDto> getTargetAmountCheckExceedRequestDtos) {
    List<GetTargetAmountCheckExceedResponseDto> getTargetAmountCheckExceedResponseDtos =
        projectClientService.getTargetAmountCheckExceed(getTargetAmountCheckExceedRequestDtos);

    return ResponseEntity.ok()
        .body(
            SuccessResponse.<List<GetTargetAmountCheckExceedResponseDto>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("성공")
                .data(getTargetAmountCheckExceedResponseDtos)
                .build());
  }

  @PostMapping("/projects/achievement")
  public SuccessResponse<HashMap<String, FundingAchievementResultOfProjectResponseDto>>
      getFundingOfProject(@RequestBody List<FundingAchievementResultOfProjectRequestDto> projects) {

    HashMap<String, FundingAchievementResultOfProjectResponseDto> map =
        projectClientService.getFundingMultipleAchievementResults(projects);

    return SuccessResponse.<HashMap<String, FundingAchievementResultOfProjectResponseDto>>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("펀딩 달성 결과 조회 (목록 페이지) 성공")
        .data(map)
        .build();
  }

  @GetMapping("/projects/{projectId}/achievement")
  SuccessResponse<FundingAchievementResultOfProjectDetailResponseDto> getFundingOfProjectDetail(
      @PathVariable Long projectId, @RequestParam Long projectTargetAmount) {

    FundingAchievementResultOfProjectDetailResponseDto dto =
        projectClientService.getFundingSingleAchievementResult(projectId, projectTargetAmount);

    return SuccessResponse.<FundingAchievementResultOfProjectDetailResponseDto>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("펀딩 달성 결과 조회 (상세 페이지) 성공")
        .data(dto)
        .build();
  }

  @PostMapping("/fundings/target-amount-projects-information")
  public ResponseEntity<SuccessResponse<TargetAmountAchievedResponseDto>>
      getTargetAmountAchievedProjects(
          @RequestBody List<ProjectInformationForAchievedTargetAmountRequestDto> projectInfo) {
    return ResponseEntity.ok()
        .body(
            SuccessResponse.<TargetAmountAchievedResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .data(projectClientService.getTargetAmountAchieved(projectInfo))
                .build());
  }

  @PostMapping("/fundings/registered-projects-check")
  public ResponseEntity<SuccessResponse<AchievedResultOfProjectResponseDto>>
      getRegisteredProjectDetail(
          @RequestBody ProjectAmountWithIdRequestDto projectAmountWithIdRequestDto) {
    return ResponseEntity.ok()
        .body(
            SuccessResponse.<AchievedResultOfProjectResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .data(projectClientService.getRegisteredProjectList(projectAmountWithIdRequestDto))
                .build());
  }

  @GetMapping("/fundings/{projectId}/registered-project-detail")
  public ResponseEntity<SuccessResponse<MemberInformationOfFundingResponseDto>>
      getRegisteredProject(@PathVariable Long projectId) {
    return ResponseEntity.ok()
        .body(
            SuccessResponse.<MemberInformationOfFundingResponseDto>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .data(projectClientService.getMemberFundingList(projectId))
                .build());
  }
}
