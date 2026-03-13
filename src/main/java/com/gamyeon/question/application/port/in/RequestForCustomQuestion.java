package com.gamyeon.question.application.port.in;

public interface RequestForCustomQuestion {

  void create(Long userId, Long intvId);
}
