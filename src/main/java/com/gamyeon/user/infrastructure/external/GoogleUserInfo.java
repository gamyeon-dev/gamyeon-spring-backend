package com.gamyeon.user.infrastructure.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gamyeon.user.application.port.outbound.OAuthPort;

public class GoogleUserInfo implements OAuthPort.OAuthUserInfo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @Override
    public String getProviderId() { return id; }

    @Override
    public String getEmail() { return email; }

    @Override
    public String getNickname() { return name; }
}
