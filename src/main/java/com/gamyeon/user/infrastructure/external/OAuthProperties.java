package com.gamyeon.user.infrastructure.external;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {

    private Provider google = new Provider();
    private Provider kakao = new Provider();

    public Provider getGoogle() { return google; }
    public void setGoogle(Provider google) { this.google = google; }

    public Provider getKakao() { return kakao; }
    public void setKakao(Provider kakao) { this.kakao = kakao; }

    public static class Provider {
        private String clientId;
        private String clientSecret;
        private String redirectUri;

        public String getClientId() { return clientId; }
        public void setClientId(String clientId) { this.clientId = clientId; }

        public String getClientSecret() { return clientSecret; }
        public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

        public String getRedirectUri() { return redirectUri; }
        public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }
    }
}
