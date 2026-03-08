package com.gamyeon.user.domain.user;

import com.gamyeon.core.exception.BaseException;

public class UserDomainException extends BaseException {

    public UserDomainException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
