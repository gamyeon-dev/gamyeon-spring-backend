package com.gamyeon.answer.domain;

import com.gamyeon.common.exception.BaseException;

public class AnswerException extends BaseException {

  public AnswerException(AnswerErrorCode errorCode) {
    super(errorCode);
  }
}
