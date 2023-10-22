package com.lotdiz.fundingservice.dto.request;

import javax.validation.constraints.NotNull.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPointUpdateRequestDto {
    private Long memberId;
    private Long memberPoint;
}
