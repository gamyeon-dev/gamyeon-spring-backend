package com.gamyeon.answer.domain;

import com.gamyeon.common.response.SuccessCode;
import org.springframework.http.HttpStatus;

public enum AnswerSuccessCode implements SuccessCode {
  ANSWER_UPLOAD_URL_ISSUED(HttpStatus.CREATED, "ANS-S001", "답변 영상 업로드용 presigned URL이 발급되었습니다."),
  ANSWER_REGISTERED(HttpStatus.CREATED, "ANS-S002", "답변 영상이 등록되었습니다."),
  ANSWER_ANALYSIS_REQUESTED(HttpStatus.ACCEPTED, "ANS-S003", "답변 STT 분석 요청이 접수되었습니다."),
  ANSWER_STT_CALLBACK_PROCESSED(HttpStatus.OK, "ANS-S004", "답변 STT 콜백이 처리되었습니다."),
  ANSWER_GAZE_SEGMENT_RECEIVED(HttpStatus.ACCEPTED, "ANS-S005", "답변 시선 데이터가 접수되었습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  AnswerSuccessCode(HttpStatus status, String code, String message) {
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
