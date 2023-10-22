package com.lotdiz.fundingservice.exception;

import com.lotdiz.fundingservice.exception.common.EntityNotFoundException;

public class ProjectAndProductInfoNotFoundException extends EntityNotFoundException {
  private static final String message = "프로젝트와 상품 정보를 찾을 수 없습니다.";

  public ProjectAndProductInfoNotFoundException() {
    super(message);
  }
}
