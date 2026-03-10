package com.gamyeon.user.infrastructure.web;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.common.response.SuccessCode;
import com.gamyeon.common.response.TempCode;
import com.gamyeon.user.application.port.inbound.LoginResult;
import com.gamyeon.user.application.port.inbound.OAuthLoginCommand;
import com.gamyeon.user.application.service.AuthService;
import com.gamyeon.user.domain.OAuthProvider;
import com.gamyeon.user.domain.UserDomainException;
import com.gamyeon.user.domain.UserErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/{provider}")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @PathVariable String provider,
            @Valid @RequestBody LoginRequest request) {

        OAuthProvider oAuthProvider = parseProvider(provider);
        OAuthLoginCommand command = OAuthLoginCommand.of(oAuthProvider, request.authorizationCode());
        LoginResult result = authService.login(command);
        return ApiResponse.success(new TempCode(), LoginResponse.from(result));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<LoginResponse>> reissue(
            @Valid @RequestBody ReissueRequest request) {

        LoginResult result = authService.reissue(request.refreshToken());
        return ApiResponse.success(new TempCode(), LoginResponse.from(result));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal Long userId) {

        authService.logout(userId);
        return ApiResponse.success(null);
    }

    private OAuthProvider parseProvider(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> OAuthProvider.GOOGLE;
            case "kakao" -> OAuthProvider.KAKAO;
            default -> throw new UserDomainException(UserErrorCode.INVALID_OAUTH_PROVIDER);
        };
    }

    public record LoginRequest(@NotBlank String authorizationCode) {}

    public record ReissueRequest(@NotBlank String refreshToken) {}

    public record LoginResponse(
            String accessToken,
            String refreshToken,
            UserResponse user
    ) {
        public static LoginResponse from(LoginResult result) {
            return new LoginResponse(
                    result.getAccessToken(),
                    result.getRefreshToken(),
                    UserResponse.from(result.getUser())
            );
        }
    }
}
