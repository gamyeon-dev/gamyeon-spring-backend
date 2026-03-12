package com.gamyeon.common.exception;

import com.gamyeon.common.response.ErrorCode;
import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {

    // ── V : Validation ────────────────────────────────
    INVALID_INPUT   (HttpStatus.BAD_REQUEST,            "입력값 유효성 검사에 실패했습니다.", "CMMN-V001"),
    MISSING_FIELD   (HttpStatus.BAD_REQUEST,            "필수 필드가 누락되었습니다.",       "CMMN-V002"),

    // ── A : Auth ──────────────────────────────────────
    UNAUTHORIZED    (HttpStatus.UNAUTHORIZED,           "인증이 필요합니다.",                "CMMN-A001"),
    FORBIDDEN       (HttpStatus.FORBIDDEN,              "접근 권한이 없습니다.",             "CMMN-A002"),
    EXPIRED_TOKEN   (HttpStatus.UNAUTHORIZED,           "토큰이 만료되었습니다.",            "CMMN-A003"),
    INVALID_TOKEN   (HttpStatus.UNAUTHORIZED,           "유효하지 않은 토큰입니다.",         "CMMN-A004"),

    // ── N : Not Found ─────────────────────────────────
    NOT_FOUND       (HttpStatus.NOT_FOUND,              "요청한 리소스를 찾을 수 없습니다.", "CMMN-N001"),

    // ── I : Internal ──────────────────────────────────
    INTERNAL_ERROR  (HttpStatus.INTERNAL_SERVER_ERROR,  "서버 내부 오류가 발생했습니다.",    "CMMN-I001"),
    DATABASE_ERROR  (HttpStatus.INTERNAL_SERVER_ERROR,  "데이터베이스 오류가 발생했습니다.", "CMMN-I002");

    private final HttpStatus status;
    private final String message;
    private final String code;

    CommonErrorCode(HttpStatus status, String message, String code) {
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
