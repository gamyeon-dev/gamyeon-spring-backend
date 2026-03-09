package com.gamyeon.user.domain;

import com.gamyeon.common.response.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {

    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임은 1~8자의 한글, 영어, 숫자만 허용됩니다.", "INVALID_NICKNAME"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.", "USER_NOT_FOUND"),
    USER_ALREADY_WITHDREW(HttpStatus.BAD_REQUEST, "이미 탈퇴한 유저입니다.", "USER_ALREADY_WITHDREW"),
    USER_BANNED(HttpStatus.FORBIDDEN, "정지된 계정입니다.", "USER_BANNED"),
    INVALID_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인 제공자입니다.", "INVALID_OAUTH_PROVIDER"),
    OAUTH_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "소셜 로그인 인증에 실패했습니다.", "OAUTH_AUTHENTICATION_FAILED"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", "INVALID_TOKEN"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.", "EXPIRED_TOKEN"),
    TOKEN_REISSUE_FAILED(HttpStatus.UNAUTHORIZED, "토큰 재발급에 실패했습니다.", "TOKEN_REISSUE_FAILED");

    private final HttpStatus status;
    private final String message;
    private final String code;

    UserErrorCode(HttpStatus status, String message, String code) {
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
