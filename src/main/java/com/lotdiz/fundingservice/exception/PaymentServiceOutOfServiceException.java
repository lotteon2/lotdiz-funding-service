package com.lotdiz.fundingservice.exception;

public class PaymentServiceOutOfServiceException extends RuntimeException {

  private static final String message = "결제 서비스가 정상적으로 작동하지 않습니다.";

  public PaymentServiceOutOfServiceException() {
    super(message);
  }
}
