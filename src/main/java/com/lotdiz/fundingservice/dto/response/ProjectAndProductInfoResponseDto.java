package com.lotdiz.fundingservice.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectAndProductInfoResponseDto {
  private Long projectId;
  private String projectDescription;
  private List<ProductInfoResponseDto> products;
}
