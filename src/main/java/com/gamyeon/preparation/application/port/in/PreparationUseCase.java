package com.gamyeon.preparation.application.port.in;

public interface PreparationUseCase {

  void create(Long intvId);

  PreparationResult get(Long userId, Long intvId);
}
