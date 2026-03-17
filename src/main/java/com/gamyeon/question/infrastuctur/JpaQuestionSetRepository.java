package com.gamyeon.question.infrastuctur;

import com.gamyeon.question.domain.QuestionSet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionSetRepository extends JpaRepository<QuestionSet, Long> {

  List<QuestionSet> findAllByIntvId(Long intvId);
}
