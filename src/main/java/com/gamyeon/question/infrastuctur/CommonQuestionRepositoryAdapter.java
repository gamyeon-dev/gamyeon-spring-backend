package com.gamyeon.question.infrastuctur;

import com.gamyeon.question.domain.CommonQuestion;
import com.gamyeon.question.domain.CommonQuestionRepository;
import com.gamyeon.question.domain.CommonQuestionStatus;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CommonQuestionRepositoryAdapter implements CommonQuestionRepository {

  private final JpaCommonQuestionSetRepository jpaCommonQuestionSetRepository;

  public CommonQuestionRepositoryAdapter(
      JpaCommonQuestionSetRepository jpaCommonQuestionSetRepository) {
    this.jpaCommonQuestionSetRepository = jpaCommonQuestionSetRepository;
  }

  @Override
  public List<CommonQuestion> getActiveQuestions() {
    return jpaCommonQuestionSetRepository.findAllByStatus(CommonQuestionStatus.ACTIVE);
  }
}
