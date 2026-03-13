package com.gamyeon.question.domain;

import com.gamyeon.common.response.SuccessCode;
import org.springframework.http.HttpStatus;

public enum QuestionSuccessCode implements SuccessCode {
  REQUEST_SUCCESS(HttpStatus.ACCEPTED, "QSTN-S001", "질문 생성이 요청되었습니다");

  private final HttpStatus status;
  private final String code;
  private final String message;

  QuestionSuccessCode(HttpStatus status, String code, String message) {
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
