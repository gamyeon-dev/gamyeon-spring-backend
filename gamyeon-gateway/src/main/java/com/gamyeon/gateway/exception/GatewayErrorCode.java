package com.gamyeon.gateway.exception;

import com.gamyeon.core.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum GatewayErrorCode implements ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", "INVALID_TOKEN"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.", "EXPIRED_TOKEN"),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "인증 토큰이 없습니다.", "MISSING_TOKEN"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서비스를 사용할 수 없습니다.", "SERVICE_UNAVAILABLE"),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "게이트웨이 타임아웃이 발생했습니다.", "GATEWAY_TIMEOUT");

    private final HttpStatus status;
    private final String message;
    private final String code;

    GatewayErrorCode(HttpStatus status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
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
