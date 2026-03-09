package com.gamyeon.user.application.service;

import com.gamyeon.user.application.port.inbound.LoginResult;
import com.gamyeon.user.application.port.inbound.OAuthLoginCommand;
import com.gamyeon.user.application.port.inbound.UserInfo;
import com.gamyeon.user.application.port.outbound.OAuthPort;
import com.gamyeon.user.application.port.outbound.RefreshTokenRepository;
import com.gamyeon.user.application.port.outbound.UserRepository;
import com.gamyeon.user.domain.OAuthProvider;
import com.gamyeon.user.domain.RefreshToken;
import com.gamyeon.user.domain.User;
import com.gamyeon.user.domain.UserDomainException;
import com.gamyeon.user.domain.UserErrorCode;
import com.gamyeon.user.infrastructure.security.JwtProvider;

public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuthPort oAuthPort;
    private final JwtProvider jwtProvider;
    private final NicknameResolver nicknameResolver;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       OAuthPort oAuthPort,
                       JwtProvider jwtProvider,
                       NicknameResolver nicknameResolver) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.oAuthPort = oAuthPort;
        this.jwtProvider = jwtProvider;
        this.nicknameResolver = nicknameResolver;
    }

    public LoginResult login(OAuthLoginCommand command) {
        OAuthProvider provider = command.getProvider();
        String authCode = command.getAuthorizationCode();

        String oauthAccessToken = oAuthPort.getAccessToken(provider, authCode);
        OAuthPort.OAuthUserInfo oAuthUserInfo = oAuthPort.getUserInfo(provider, oauthAccessToken);

        String nickname = nicknameResolver.resolve(oAuthUserInfo.getNickname(), oAuthUserInfo.getEmail());

        User user = userRepository.findByProviderAndProviderId(provider, oAuthUserInfo.getProviderId())
                .orElseGet(() -> {
                    User newUser = User.create(
                            oAuthUserInfo.getEmail(),
                            nickname,
                            provider,
                            oAuthUserInfo.getProviderId()
                    );
                    return userRepository.save(newUser);
                });

        ensureLoginAllowed(user);

        return issueTokens(user);
    }

    public LoginResult reissue(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new UserDomainException(UserErrorCode.TOKEN_REISSUE_FAILED));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.deleteByUserId(refreshToken.getUserId());
            throw new UserDomainException(UserErrorCode.TOKEN_REISSUE_FAILED);
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new UserDomainException(UserErrorCode.USER_NOT_FOUND));

        ensureLoginAllowed(user);

        refreshTokenRepository.deleteByUserId(user.getId());
        return issueTokens(user);
    }

    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    private void ensureLoginAllowed(User user) {
        if (user.isBanned()) {
            throw new UserDomainException(UserErrorCode.USER_BANNED);
        }
        if (user.isWithdrew()) {
            throw new UserDomainException(UserErrorCode.USER_ALREADY_WITHDREW);
        }
    }

    private LoginResult issueTokens(User user) {
        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshTokenValue = jwtProvider.createRefreshToken(user.getId());

        RefreshToken refreshToken = RefreshToken.create(
                user.getId(),
                refreshTokenValue,
                jwtProvider.getRefreshTokenExpiry()
        );
        refreshTokenRepository.save(refreshToken);

        return LoginResult.of(accessToken, refreshTokenValue, UserInfo.from(user));
    }
}
