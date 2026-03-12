package com.gamyeon.question.infrastuctur;

import com.gamyeon.question.domain.QuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionSetRepository extends JpaRepository<QuestionSet, Long> {
}
