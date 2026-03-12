package com.gamyeon.question.infrastuctur;

import com.gamyeon.question.domain.CommonQuestion;
import com.gamyeon.question.domain.CommonQuestionRepository;
import com.gamyeon.question.domain.CommonQuestionStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommonQuestionRepositoryAdapter implements CommonQuestionRepository {

    private final JpaCommonQuestionSetRepository jpaCommonQuestionSetRepository;

    public CommonQuestionRepositoryAdapter(JpaCommonQuestionSetRepository jpaCommonQuestionSetRepository) {
        this.jpaCommonQuestionSetRepository = jpaCommonQuestionSetRepository;
    }

    @Override
    public List<CommonQuestion> getActiveQuestions() {
        return jpaCommonQuestionSetRepository.findAllByStatus(CommonQuestionStatus.ACTIVE);
    }
}
