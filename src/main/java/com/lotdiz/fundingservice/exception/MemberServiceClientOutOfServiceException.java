package com.lotdiz.fundingservice.exception;

public class MemberServiceClientOutOfServiceException extends RuntimeException {

  private static final String message = "멤버 서비스가 정상적으로 작동중이지 않습니다.";

  public MemberServiceClientOutOfServiceException() {
    super(message);
  }
}
