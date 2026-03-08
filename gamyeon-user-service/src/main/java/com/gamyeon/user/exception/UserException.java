package com.gamyeon.user.exception;

import com.gamyeon.core.exception.BaseException;

public class UserException extends BaseException {

    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
