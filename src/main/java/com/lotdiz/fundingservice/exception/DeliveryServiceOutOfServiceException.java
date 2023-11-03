package com.lotdiz.fundingservice.exception;

public class DeliveryServiceOutOfServiceException extends RuntimeException {

private static final String message = "배송 서비스가 정상적으로 작동하지 않습니다.";

public DeliveryServiceOutOfServiceException() {
    super(message);
}
}