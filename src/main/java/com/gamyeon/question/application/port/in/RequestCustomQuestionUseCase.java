package com.gamyeon.question.application.port.in;

public interface RequestCustomQuestionUseCase {

  void create(Long userId, Long intvId);
}
