package com.gamyeon.user.application.port.inbound;

public class NicknameUpdateCommand {

  private final Long userId;
  private final String nickname;

  private NicknameUpdateCommand(Long userId, String nickname) {
    this.userId = userId;
    this.nickname = nickname;
  }

  public static NicknameUpdateCommand of(Long userId, String nickname) {
    return new NicknameUpdateCommand(userId, nickname);
  }

  public Long getUserId() {
    return userId;
  }

  public String getNickname() {
    return nickname;
  }
}
