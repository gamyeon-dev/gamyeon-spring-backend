package com.gamyeon.common.response;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMN-V001", "입력값 유효성 검사에 실패했습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMN-C001", "서버 내부 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  CommonErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
