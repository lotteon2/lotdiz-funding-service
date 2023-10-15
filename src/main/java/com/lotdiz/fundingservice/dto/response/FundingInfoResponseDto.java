package com.lotdiz.fundingservice.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FundingInfoResponseDto {
  // project정보, member정보, delivery정보, payments정보 필드 추가
  private Long fundingId;
  private Long projectId;
  private String projectName;
  private String projectStatus;
  private String projectImageUrl;
  private String makerName;
  private Long projectParticipantCount;
  private Long totalPaymentAmount;
  private LocalDateTime paymentDate;
  private Long totalAccumulatedFundingAmount;
  private Long fundingAchievementRate;
  private Long remainingDays;
  private String fundingStatus;
  private String deliveryStatus;
}
