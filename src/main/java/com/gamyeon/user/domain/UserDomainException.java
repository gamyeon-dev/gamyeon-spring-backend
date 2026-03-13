package com.gamyeon.user.domain;

import com.gamyeon.common.exception.BaseException;

public class UserDomainException extends BaseException {

  public UserDomainException(UserErrorCode errorCode) {
    super(errorCode);
  }
}
