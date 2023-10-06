package com.lotdiz.fundingservice.entity;

import lombok.Getter;

@Getter
public enum FundingStatus {
  COMPLETED("펀딩 완료"),
  CANCELED("펀딩 취소");

  private final String message;

  FundingStatus(String message) {
    this.message = message;
  }
}
