package com.gamyeon.intv.domain;

import com.gamyeon.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum IntvErrorCode implements ErrorCode {

    DO_NOT_PAUSE(HttpStatus.BAD_REQUEST, "INTV_B001", "진행 중인 면접만 중단할 수 있습니다."),
    DO_NOT_RESUME(HttpStatus.BAD_REQUEST, "INTV_B002", "중단된 면접만 재개할 수 있습니다."),
    DO_NOT_FINISH(HttpStatus.BAD_REQUEST, "INTV_B003", "진행 중인 면접만 종료할 수 있습니다."),

    ;

    private final HttpStatus status;
    private final String message;
    private final String code;

    IntvErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }
}

