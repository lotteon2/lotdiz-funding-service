package com.lotdiz.fundingservice.exception;

import com.lotdiz.fundingservice.exception.common.EntityNotFoundException;

public class ProductFundingEntityNotFoundException extends EntityNotFoundException {

  private static final String message = "펀딩 정보를 찾을 수 없습니다.";

  public ProductFundingEntityNotFoundException() {
    super(message);
  }
}
