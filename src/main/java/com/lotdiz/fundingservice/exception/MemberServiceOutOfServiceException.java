package com.lotdiz.fundingservice.exception;

public class MemberServiceOutOfServiceException extends RuntimeException {

  private static final String message = "회원 서비스가 정상적으로 작동하지 않습니다.";

  public MemberServiceOutOfServiceException() {
    super(message);
  }
}
