package com.lotdiz.fundingservice.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberFundingInformationDto {
  private Long memberId;
  private Long fundingId;
  private Long fundingTotalAmount;
  private LocalDateTime fundingDate;
}
