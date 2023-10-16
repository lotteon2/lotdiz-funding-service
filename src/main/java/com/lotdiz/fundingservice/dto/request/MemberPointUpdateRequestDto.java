package com.lotdiz.fundingservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberPointUpdateRequestDto {
    private Long memberId;
    private Long memberPoint;
}
