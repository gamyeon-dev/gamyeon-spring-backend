package com.gamyeon.question.application.port.in;

public interface GetQuestionSetUseCase {

  GetQuestionSetResult getQuestionSets(Long userId, Long intvId);
}
