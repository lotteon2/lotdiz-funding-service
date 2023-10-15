package com.lotdiz.fundingservice.exception;

import com.lotdiz.fundingservice.exception.common.EntityNotFoundException;

public class PaymentInfoNotFoundException extends EntityNotFoundException {
  private static final String message = "결제 정보를 찾을 수 없습니다.";

  public PaymentInfoNotFoundException() {
    super(message);
  }
}
