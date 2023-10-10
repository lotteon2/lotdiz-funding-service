package com.lotdiz.fundingservice.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetTargetAmountCheckExceedRequestDto {

  @NotNull(message = "projectId cannot be null")
  private Long projectId;

  @NotBlank(message = "projectName cannot be blank")
  private String projectName;

  @NotNull(message = "makerMemberId cannot be null")
  private Long makerMemberId;

  @NotNull(message = "projectTargetAmount cannot be null")
  private Long projectTargetAmount;
}
