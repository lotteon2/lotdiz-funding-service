package com.lotdiz.fundingservice.controller.client;

import com.lotdiz.fundingservice.dto.request.GetTargetAmountCheckExceedRequestDto;
import com.lotdiz.fundingservice.dto.request.MemberInformationOfFundingResponseDto;
import com.lotdiz.fundingservice.dto.response.GetTargetAmountCheckExceedResponseDto;
import com.lotdiz.fundingservice.service.ProjectClientService;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
