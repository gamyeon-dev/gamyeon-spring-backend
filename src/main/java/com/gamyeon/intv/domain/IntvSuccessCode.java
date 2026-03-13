package com.gamyeon.intv.domain;

import com.gamyeon.common.response.SuccessCode;
import org.springframework.http.HttpStatus;

public enum IntvSuccessCode implements SuccessCode {
  INTV_CREATED(HttpStatus.CREATED, "INTV-S001", "면접이 생성되었습니다."),
  INTV_STARTED(HttpStatus.OK, "INTV-S002", "면접이 시작되었습니다."),
  INTV_PAUSED(HttpStatus.OK, "INTV-S003", "면접이 중단되었습니다."),
  INTV_RESUMED(HttpStatus.OK, "INTV-S004", "면접이 재개되었습니다."),
  INTV_FINISHED(HttpStatus.OK, "INTV-S005", "면접이 종료되었습니다."),
  INTV_UPDATED(HttpStatus.OK, "INTV-S006", "면접 제목이 수정되었습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  IntvSuccessCode(HttpStatus status, String code, String message) {
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
