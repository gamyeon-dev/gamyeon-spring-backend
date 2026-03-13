package com.gamyeon.user.application.port.inbound;

public interface UserUseCase {

  UserInfo getMyInfo(Long userId);

  UserInfo updateNickname(NicknameUpdateCommand command);

  void withdraw(Long userId);
}
