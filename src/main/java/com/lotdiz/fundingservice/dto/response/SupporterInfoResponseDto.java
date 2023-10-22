package com.lotdiz.fundingservice.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupporterInfoResponseDto {
  private Long supportWithUsId;
  private Boolean supporterWithUsIsNamePublic;
  private Boolean supporterWithUsIsAmountPublic;
  private Long memberId;
  private String memberName;
  private String memberProfileImageUrl;
  private Long fundingtTotalAmount;
  private Long fundingSupportAmount;
  private LocalDateTime createdAt;
}
