package com.lotdiz.fundingservice.exception;

public class ProjectServiceOutOfServiceException extends RuntimeException {

  private static final String message = "프로젝트 서비스가 정상적으로 작동하지 않습니다.";

  public ProjectServiceOutOfServiceException() {
    super(message);
  }
}
