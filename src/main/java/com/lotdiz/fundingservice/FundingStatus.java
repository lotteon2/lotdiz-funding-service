package com.lotdiz.fundingservice;

import lombok.Getter;

@Getter
public enum FundingStatus {
    COMPLETED("완료"),
    CANCELED("취소");

    private final String message;

    FundingStatus(String message) {
        this.message = message;
    }
}
