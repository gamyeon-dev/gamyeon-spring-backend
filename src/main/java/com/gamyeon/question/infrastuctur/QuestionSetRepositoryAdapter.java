package com.gamyeon.question.infrastuctur;

import com.gamyeon.question.domain.QuestionSet;
import com.gamyeon.question.domain.QuestionSetRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionSetRepositoryAdapter implements QuestionSetRepository {

  private final JpaQuestionSetRepository jpaQuestionSetRepository;

  public QuestionSetRepositoryAdapter(JpaQuestionSetRepository jpaQuestionSetRepository) {
    this.jpaQuestionSetRepository = jpaQuestionSetRepository;
  }

  @Override
  public void save(QuestionSet questionSet) {
    jpaQuestionSetRepository.save(questionSet);
  }
}
