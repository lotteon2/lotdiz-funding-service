package com.lotdiz.fundingservice.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private String code;
  private String message;
  private String detail;
}
