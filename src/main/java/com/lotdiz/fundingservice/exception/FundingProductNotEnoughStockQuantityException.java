package com.lotdiz.fundingservice.exception;

import com.lotdiz.fundingservice.exception.common.DomainException;
import javax.servlet.http.HttpServletResponse;

public class FundingProductNotEnoughStockQuantityException extends DomainException {
  private static final String message = "상품의 재고가 부족합니다.";

  public FundingProductNotEnoughStockQuantityException() {
    super(message);
  }

  @Override
  public int getStatusCode() {
    return HttpServletResponse.SC_NOT_FOUND;
  }
}
