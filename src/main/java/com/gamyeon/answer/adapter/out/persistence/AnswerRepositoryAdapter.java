package com.gamyeon.answer.adapter.out.persistence;

import com.gamyeon.answer.domain.Answer;
import com.gamyeon.answer.domain.AnswerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryAdapter implements AnswerRepository {

  private final JpaAnswerRepository jpaAnswerRepository;

  @Override
  public Answer save(Answer answer) {
    return jpaAnswerRepository.save(answer);
  }

  @Override
  public Optional<Answer> findById(Long answerId) {
    return jpaAnswerRepository.findById(answerId);
  }

  @Override
  public Optional<Answer> findByQuestionSetId(Long questionSetId) {
    return jpaAnswerRepository.findByQuestionSetId(questionSetId);
  }
}
