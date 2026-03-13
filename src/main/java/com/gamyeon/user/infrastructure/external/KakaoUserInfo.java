package com.gamyeon.user.infrastructure.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gamyeon.user.application.port.outbound.OAuthPort;

public class KakaoUserInfo implements OAuthPort.OAuthUserInfo {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  @Override
  public String getProviderId() {
    return String.valueOf(id);
  }

  @Override
  public String getEmail() {
    if (kakaoAccount == null) return null;
    return kakaoAccount.email;
  }

  @Override
  public String getNickname() {
    if (kakaoAccount == null || kakaoAccount.profile == null) return null;
    return kakaoAccount.profile.nickname;
  }

  public static class KakaoAccount {
    @JsonProperty("email")
    private String email;

    @JsonProperty("profile")
    private Profile profile;

    public static class Profile {
      @JsonProperty("nickname")
      private String nickname;
    }
  }
}
