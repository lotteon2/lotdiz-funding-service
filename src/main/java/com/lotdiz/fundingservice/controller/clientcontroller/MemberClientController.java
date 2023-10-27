package com.lotdiz.fundingservice.controller.clientcontroller;

import com.lotdiz.fundingservice.dto.response.FundingAchievementResultForMemberResponseDto;
import com.lotdiz.fundingservice.service.MemberClientService;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberClientController {
  private final MemberClientService memberClientService;
  @GetMapping("/likes/detail")
  SuccessResponse<List<FundingAchievementResultForMemberResponseDto>> getFundingInfoForMember(
      @RequestParam List<Long> projectId) {

    List<FundingAchievementResultForMemberResponseDto> fundingInfoForMemberService =
        memberClientService.getFundingInfoForMemberService(projectId);

    return SuccessResponse.<List<FundingAchievementResultForMemberResponseDto>>builder()
        .code(String.valueOf(HttpStatus.OK.value()))
        .message(HttpStatus.OK.name())
        .detail("펀딩 달성 정보 응답 (멤버 서비스)")
        .data(fundingInfoForMemberService)
        .build();
  }
}
