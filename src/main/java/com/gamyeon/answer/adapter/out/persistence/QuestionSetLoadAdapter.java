package com.gamyeon.answer.adapter.out.persistence;

import com.gamyeon.answer.application.port.out.LoadQuestionSetPort;
import com.gamyeon.question.infrastucture.JpaQuestionSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionSetLoadAdapter implements LoadQuestionSetPort {

  private final JpaQuestionSetRepository jpaQuestionSetRepository;

  @Override
  public boolean existsById(Long questionSetId) {
    return jpaQuestionSetRepository.existsById(questionSetId);
  }
}
