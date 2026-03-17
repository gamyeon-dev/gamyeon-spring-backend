package com.gamyeon.answer.application.port.out;

public interface LoadQuestionSetPort {

  boolean existsById(Long questionSetId);

  String getQuestionContent(Long questionSetId);
}
