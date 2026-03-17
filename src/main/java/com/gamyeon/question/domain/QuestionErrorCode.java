package com.gamyeon.question.domain;

import com.gamyeon.common.response.ErrorCode;
import org.springframework.http.HttpStatus;

public enum QuestionErrorCode implements ErrorCode {
  ALREADY_EXIST(HttpStatus.BAD_REQUEST, "QSTN-B001", "해당 면접 세션에 이미 질문이 존재합니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  QuestionErrorCode(HttpStatus status, String code, String message) {
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
