package com.gamyeon.user.infrastructure.web;

import com.gamyeon.user.application.port.inbound.UserInfo;
import com.gamyeon.user.domain.OAuthProvider;
import com.gamyeon.user.domain.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        OAuthProvider provider,
        UserStatus status,
        LocalDateTime createdAt
) {
    public static UserResponse from(UserInfo userInfo) {
        return new UserResponse(
                userInfo.getId(),
                userInfo.getEmail(),
                userInfo.getNickname(),
                userInfo.getProvider(),
                userInfo.getStatus(),
                userInfo.getCreatedAt()
        );
    }
}
