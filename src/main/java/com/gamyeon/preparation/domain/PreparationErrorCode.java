package com.gamyeon.preparation.domain;

import com.gamyeon.common.response.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PreparationErrorCode implements ErrorCode {
    DO_NOT_READY(HttpStatus.BAD_REQUEST, "PREP-B001", "준비 상태가 아니면 READY로 변경할 수 없습니다."),
    DO_NOT_GENERATING(HttpStatus.BAD_REQUEST, "PREP-B002", "READY 상태에서만 질문 생성을 시작할 수 있습니다."),
    DO_NOT_GENERATED(HttpStatus.BAD_REQUEST, "PREP-B003", "질문 생성 중일 때만 생성 완료 처리할 수 있습니다."),
    DO_NOT_FAILED(HttpStatus.BAD_REQUEST, "PREP-B004", "질문 생성 중일 때만 실패 처리할 수 있습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "PREP-B005", "PDF 파일만 업로드할 수 있습니다."),
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "PREP-B006", "contentType은 application/pdf 이어야 합니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "PREP-B007", "파일 크기가 허용 용량을 초과했습니다."),
    PREPARATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PREP-N001", "해당 면접의 preparation을 찾을 수 없습니다."),
    PREPARATION_FILE_TYPE_ALREADY_EXISTS(HttpStatus.CONFLICT, "PREP-C001", "해당 파일 타입은 이미 업로드되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    PreparationErrorCode(HttpStatus status, String code, String message) {
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
