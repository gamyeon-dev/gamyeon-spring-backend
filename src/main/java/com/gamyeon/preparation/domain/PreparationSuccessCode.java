package com.gamyeon.preparation.domain;

import com.gamyeon.common.response.SuccessCode;
import org.springframework.http.HttpStatus;

public enum PreparationSuccessCode implements SuccessCode {
    PREPARATION_UPLOAD_URL_ISSUED(HttpStatus.CREATED, "PREP-S001", "파일 업로드용 presigned URL이 발급되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    PreparationSuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
