package com.gamyeon.intv.domain;

import com.gamyeon.common.exception.BaseException;

public class IntvException extends BaseException {

  public IntvException(IntvErrorCode errorCode) {
    super(errorCode);
  }
}
