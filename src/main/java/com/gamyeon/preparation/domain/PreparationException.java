package com.gamyeon.preparation.domain;

import com.gamyeon.common.exception.BaseException;

public class PreparationException extends BaseException {

    public PreparationException(PreparationErrorCode errorCode) {
        super(errorCode);
    }
}

