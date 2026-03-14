package com.gamyeon.feedback.application.exception;

public class QuestionSetNotFoundException extends RuntimeException {
  public QuestionSetNotFoundException(Long questionSetId) {
    super("QuestionSet을 찾을 수 없습니다. id=" + questionSetId);
  }
}
