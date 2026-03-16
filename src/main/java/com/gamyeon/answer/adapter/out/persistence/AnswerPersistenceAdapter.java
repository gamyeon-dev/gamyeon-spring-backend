package com.gamyeon.answer.adapter.out.persistence;

import com.gamyeon.answer.domain.Answer;
import com.gamyeon.answer.domain.AnswerRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository // 이 어노테이션이 있어야 AnswerLoadAdapter에 주입됩니다.
@RequiredArgsConstructor
public class AnswerPersistenceAdapter implements AnswerRepository {

  private final JpaAnswerRepository jpaRepository;

  @Override
  public Answer save(Answer answer) {
    return jpaRepository.save(answer);
  }

  @Override
  public Optional<Answer> findById(Long answerId) {
    return jpaRepository.findById(answerId);
  }

  @Override
  public Optional<Answer> findByQuestionSetId(Long questionSetId) {
    return jpaRepository.findByQuestionSetId(questionSetId);
  }

  @Override
  public List<Answer> findByIntvId(Long intvId) {
    return jpaRepository.findAllByIntvId(intvId);
  }
}
