package com.lotdiz.fundingservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessResponse<T> {

  private String code;
  private String message;
  private String detail;

  @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
  private T data;
}
