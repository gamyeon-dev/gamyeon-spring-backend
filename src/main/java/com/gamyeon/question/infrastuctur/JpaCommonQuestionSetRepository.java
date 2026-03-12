package com.gamyeon.question.infrastuctur;

import com.gamyeon.question.domain.CommonQuestion;
import com.gamyeon.question.domain.CommonQuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCommonQuestionSetRepository extends JpaRepository<CommonQuestion, Long> {

    List<CommonQuestion> findAllByStatus(CommonQuestionStatus status);
}
