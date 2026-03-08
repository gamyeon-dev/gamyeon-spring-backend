package com.gamyeon.user.application.port.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class NicknameUpdateRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,8}$", message = "닉네임은 1~8자의 한글, 영어, 숫자만 허용됩니다.")
    private String nickname;

    protected NicknameUpdateRequest() {
    }

    public NicknameUpdateRequest(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
