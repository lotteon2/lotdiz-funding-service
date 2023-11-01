package com.lotdiz.fundingservice.exceptionhandler;

import com.lotdiz.fundingservice.exception.common.DomainException;
import com.lotdiz.fundingservice.utils.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class FundingRestControllerAdvice extends ResponseEntityExceptionHandler {

  private static final String UNIQUE_CONSTRAINT_EXCEPTION_MESSAGE = "유니크 제약조건 오류";
  private static final String DUPLICATE_KEY_EXCEPTION_MESSAGE = "중복 키 오류";
  private static final String DOMAIN_EXCEPTION_MESSAGE = "도메인 오류";
  private static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE = "VALIDATION 오류";

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> domainException(DomainException e) {
    int statusCode = e.getStatusCode();

    ErrorResponse body =
        ErrorResponse.builder()
            .code(String.valueOf(statusCode))
            .message(DOMAIN_EXCEPTION_MESSAGE)
            .detail(e.getMessage())
            .build();

    return ResponseEntity.status(statusCode).body(body);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> constraintViolationException(
      DataIntegrityViolationException e) {
    log.error("{}", e.getMessage());
    int statusCode = HttpStatus.BAD_REQUEST.value();
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .code(String.valueOf(statusCode))
            .message(UNIQUE_CONSTRAINT_EXCEPTION_MESSAGE)
            .detail(e.getMessage())
            .build();

    return ResponseEntity.status(statusCode).body(errorResponse);
  }

  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<ErrorResponse> duplicateKeyException(DuplicateKeyException e) {
    log.error("{}", e.getMessage());
    int statusCode = HttpStatus.BAD_REQUEST.value();
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .code(String.valueOf(statusCode))
            .message(DUPLICATE_KEY_EXCEPTION_MESSAGE)
            .detail(e.getMessage())
            .build();

    return ResponseEntity.status(statusCode).body(errorResponse);
  }

  @NotNull
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    log.error("{}", e.getMessage());

    ErrorResponse body =
        ErrorResponse.builder()
            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .message(METHOD_ARGUMENT_NOT_VALID_EXCEPTION_MESSAGE)
            .detail(
                e.getBindingResult().getFieldError() == null
                    ? e.getMessage()
                    : e.getBindingResult().getFieldError().getDefaultMessage())
            .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exception(Exception e) {
    log.error("{}", e.getMessage());
    int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    ErrorResponse errorResponse =
        ErrorResponse.builder().code(String.valueOf(statusCode)).message(e.getMessage()).build();
    return ResponseEntity.status(statusCode).body(errorResponse);
  }
}
