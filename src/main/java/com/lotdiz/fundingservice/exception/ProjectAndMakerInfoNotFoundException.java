package com.lotdiz.fundingservice.exception;

import com.lotdiz.fundingservice.exception.common.EntityNotFoundException;

public class ProjectAndMakerInfoNotFoundException extends EntityNotFoundException {
  private static final String message = "프로젝트와 메이커 정보를 찾을 수 없습니다.";

  public ProjectAndMakerInfoNotFoundException() {
    super(message);
  }
}
