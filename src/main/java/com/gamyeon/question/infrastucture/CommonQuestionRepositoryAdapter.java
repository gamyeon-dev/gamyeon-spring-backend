package com.gamyeon.question.infrastucture;

import com.gamyeon.question.domain.CommonQuestion;
import com.gamyeon.question.domain.CommonQuestionRepository;
import com.gamyeon.question.domain.CommonQuestionStatus;
import java.util.ArrayList;
import java.util.Collections;
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
  public List<CommonQuestion> findRandomActiveQuestions(int count) {

    List<CommonQuestion> activeQuestions =
        jpaCommonQuestionSetRepository.findAllByStatus(CommonQuestionStatus.ACTIVE);
    List<CommonQuestion> shuffled = new ArrayList<>(activeQuestions);
    Collections.shuffle(shuffled);

    return shuffled.subList(0, Math.min(count, shuffled.size()));
  }
}
