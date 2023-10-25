package com.lotdiz.fundingservice.dto.response;

import java.util.List;
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
public class FundingAndTotalPageResponseDto {
    private Long totalPages;
    private List<FundingInfoResponseDto> fundingInfoResponseDtos;
}
