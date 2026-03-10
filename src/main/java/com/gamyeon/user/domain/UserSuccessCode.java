package com.gamyeon.user.domain;

import com.gamyeon.common.response.SuccessCode;
import org.springframework.http.HttpStatus;

public enum UserSuccessCode implements SuccessCode {

    USER_LOGIN          (HttpStatus.OK,      "USER-S001", "로그인에 성공했습니다."),
    USER_TOKEN_REISSUED (HttpStatus.OK,      "USER-S002", "토큰이 재발급되었습니다."),
    USER_LOGOUT         (HttpStatus.OK,      "USER-S003", "로그아웃 되었습니다."),
    USER_NICKNAME_UPDATED(HttpStatus.OK,     "USER-S004", "닉네임이 수정되었습니다."),
    USER_WITHDREW       (HttpStatus.OK,      "USER-S005", "회원 탈퇴가 완료되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    UserSuccessCode(HttpStatus status, String code, String message) {
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
