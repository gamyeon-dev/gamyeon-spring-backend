package com.gamyeon.question.infrastucture;

import com.gamyeon.question.domain.CommonQuestion;
import com.gamyeon.question.domain.CommonQuestionStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommonQuestionSetRepository extends JpaRepository<CommonQuestion, Long> {

    List<CommonQuestion> findAllByStatus(CommonQuestionStatus status);
}
