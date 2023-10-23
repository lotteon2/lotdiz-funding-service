package com.lotdiz.fundingservice.dto.request;

import com.lotdiz.fundingservice.dto.MemberFundingInformationDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberInformationOfFundingResponseDto {
  private List<MemberFundingInformationDto> memberFundingInformationDtos;
}