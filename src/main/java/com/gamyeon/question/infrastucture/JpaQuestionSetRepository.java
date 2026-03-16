package com.gamyeon.question.infrastucture;

import com.gamyeon.question.domain.QuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaQuestionSetRepository extends JpaRepository<QuestionSet, Long> {

  List<QuestionSet> findAllByIntvId(Long intvId);

  boolean existsByIntvId(Long intvId);
}
