package com.lotdiz.fundingservice.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetTargetAmountCheckExceedResponseDto {

  private Long projectId;
  private String projectName;
  private Long makerMemberId;
  private Boolean isTargetAmountExceed;
  private List<Long> memberIds;
}
