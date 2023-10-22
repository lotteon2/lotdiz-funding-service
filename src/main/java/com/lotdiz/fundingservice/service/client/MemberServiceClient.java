package com.lotdiz.fundingservice.service.client;

import com.lotdiz.fundingservice.dto.request.MemberPointUpdateRequestDto;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="memberServiceClient", url="${endpoint.member-service}")
public interface MemberServiceClient {
  @PutMapping("/members/point")
  SuccessResponse usePoint(@RequestBody MemberPointUpdateRequestDto memberPointUpdateRequestDto);
}
