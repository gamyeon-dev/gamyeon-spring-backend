package com.gamyeon.user.application.port.inbound;

import com.gamyeon.user.domain.OAuthProvider;

public class OAuthLoginCommand {

    private final OAuthProvider provider;
    private final String authorizationCode;

    private OAuthLoginCommand(OAuthProvider provider, String authorizationCode) {
        this.provider = provider;
        this.authorizationCode = authorizationCode;
    }

    public static OAuthLoginCommand of(OAuthProvider provider, String authorizationCode) {
        return new OAuthLoginCommand(provider, authorizationCode);
    }

    public OAuthProvider getProvider() { return provider; }
    public String getAuthorizationCode() { return authorizationCode; }
}
