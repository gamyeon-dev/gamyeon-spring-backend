package com.gamyeon.user.infrastructure.external;

import com.gamyeon.common.exception.CommonErrorCode;
import com.gamyeon.common.exception.CommonException;
import com.gamyeon.user.application.port.outbound.OAuthPort;
import com.gamyeon.user.domain.OAuthProvider;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class OAuthAdapter implements OAuthPort {

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final WebClient webClient;
    private final OAuthProperties oAuthProperties;

    public OAuthAdapter(WebClient webClient, OAuthProperties oAuthProperties) {
        this.webClient = webClient;
        this.oAuthProperties = oAuthProperties;
    }

    @Override
    public String getAccessToken(OAuthProvider provider, String authorizationCode) {
        try {
            return switch (provider) {
                case GOOGLE -> fetchGoogleAccessToken(authorizationCode);
                case KAKAO -> fetchKakaoAccessToken(authorizationCode);
            };
        } catch (WebClientResponseException e) {
            throw new CommonException(CommonErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public OAuthUserInfo getUserInfo(OAuthProvider provider, String accessToken) {
        try {
            return switch (provider) {
                case GOOGLE -> fetchGoogleUserInfo(accessToken);
                case KAKAO -> fetchKakaoUserInfo(accessToken);
            };
        } catch (WebClientResponseException e) {
            throw new CommonException(CommonErrorCode.UNAUTHORIZED);
        }
    }

    private String fetchGoogleAccessToken(String authorizationCode) {
        OAuthProperties.Provider google = oAuthProperties.getGoogle();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", google.getClientId());
        params.add("client_secret", google.getClientSecret());
        params.add("redirect_uri", google.getRedirectUri());
        params.add("grant_type", "authorization_code");

        TokenResponse response = webClient.post()
                .uri(GOOGLE_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        return response.accessToken();
    }

    private GoogleUserInfo fetchGoogleUserInfo(String accessToken) {
        return webClient.get()
                .uri(GOOGLE_USERINFO_URL)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GoogleUserInfo.class)
                .block();
    }

    private String fetchKakaoAccessToken(String authorizationCode) {
        OAuthProperties.Provider kakao = oAuthProperties.getKakao();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", kakao.getClientId());
        params.add("client_secret", kakao.getClientSecret());
        params.add("redirect_uri", kakao.getRedirectUri());
        params.add("grant_type", "authorization_code");

        TokenResponse response = webClient.post()
                .uri(KAKAO_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        return response.accessToken();
    }

    private KakaoUserInfo fetchKakaoUserInfo(String accessToken) {
        return webClient.get()
                .uri(KAKAO_USERINFO_URL)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();
    }

    private record TokenResponse(
            @com.fasterxml.jackson.annotation.JsonProperty("access_token") String accessToken
    ) {}
}
