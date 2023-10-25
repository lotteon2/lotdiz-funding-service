package com.lotdiz.fundingservice.exception;

import com.lotdiz.fundingservice.exception.common.EntityNotFoundException;

public class FundingEntityNotFoundException extends EntityNotFoundException {

private static final String message = "펀딩 정보를 찾을 수 없습니다.";

public FundingEntityNotFoundException() {
    super(message);
}
}