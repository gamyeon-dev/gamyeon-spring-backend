package com.gamyeon.user.application.port.inbound;

import com.gamyeon.user.domain.user.OAuthProvider;
import com.gamyeon.user.domain.user.User;
import com.gamyeon.user.domain.user.UserStatus;

import java.time.LocalDateTime;

public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final OAuthProvider provider;
    private final UserStatus status;
    private final LocalDateTime createdAt;

    private UserResponse(Long id, String email, String nickname, OAuthProvider provider,
                         UserStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProvider(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public OAuthProvider getProvider() { return provider; }
    public UserStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
