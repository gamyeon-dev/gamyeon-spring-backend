package com.gamyeon.question.domain;

import com.gamyeon.common.exception.BaseException;

public class QuestionException extends BaseException {
  public QuestionException(QuestionErrorCode errorCode) {
    super(errorCode);
  }
}
