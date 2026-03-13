package com.gamyeon.user.application.port.inbound;

public class LoginResult {

  private final String accessToken;
  private final String refreshToken;
  private final UserInfo user;

  private LoginResult(String accessToken, String refreshToken, UserInfo user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.user = user;
  }

  public static LoginResult of(String accessToken, String refreshToken, UserInfo user) {
    return new LoginResult(accessToken, refreshToken, user);
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public UserInfo getUser() {
    return user;
  }
}
