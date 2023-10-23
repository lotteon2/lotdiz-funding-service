package com.lotdiz.fundingservice.exception;

import com.lotdiz.fundingservice.exception.common.DomainException;
import javax.servlet.http.HttpServletResponse;

public class FundingNotRefundableException extends DomainException {
private static final String message = "해당 펀딩은 취소나 환불이 불가능합니다.";

public FundingNotRefundableException() {
    super(message);
}

@Override
public int getStatusCode() {
    return HttpServletResponse.SC_NOT_FOUND;
}
}