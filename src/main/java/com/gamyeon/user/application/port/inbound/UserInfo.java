package com.gamyeon.user.application.port.inbound;

import com.gamyeon.user.domain.OAuthProvider;
import com.gamyeon.user.domain.User;
import com.gamyeon.user.domain.UserStatus;

import java.time.LocalDateTime;

public class UserInfo {

    private final Long id;
    private final String email;
    private final String nickname;
    private final OAuthProvider provider;
    private final UserStatus status;
    private final LocalDateTime createdAt;

    private UserInfo(Long id, String email, String nickname, OAuthProvider provider,
                     UserStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static UserInfo from(User user) {
        return new UserInfo(
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
