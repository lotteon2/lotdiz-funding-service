package com.lotdiz.fundingservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessResponse {
    private String code;
    private String message;
    private String detail;

    @JsonInclude(value= Include.NON_EMPTY)
    private Object data;
}
