package com.gamyeon.gateway.exception;

import com.gamyeon.core.exception.BaseException;

public class GatewayException extends BaseException {

    public GatewayException(GatewayErrorCode errorCode) {
        super(errorCode);
    }
}
