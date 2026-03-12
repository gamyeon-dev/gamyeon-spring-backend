package com.gamyeon.question.application;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvErrorCode;
import com.gamyeon.intv.domain.IntvException;
import com.gamyeon.question.application.port.in.SaveQuestionSetUseCase;
import com.gamyeon.question.application.port.out.LoadIntvPort;
import com.gamyeon.question.application.port.out.LoadPreparationPort;
import com.gamyeon.question.application.port.out.PreparationForQuestionGeneration;
import com.gamyeon.question.domain.CommonQuestionRepository;
import com.gamyeon.question.domain.QuestionSetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionSetApplicationService implements SaveQuestionSetUseCase {

    private final QuestionSetRepository questionSetRepository;
    private final CommonQuestionRepository commonQuestionRepository;

    private final LoadPreparationPort loadPreparationPort;
    private final LoadIntvPort loadIntvPort;


    public QuestionSetApplicationService(QuestionSetRepository questionSetRepository,
                                         CommonQuestionRepository commonQuestionRepository,
                                         LoadPreparationPort loadPreparationPort,
                                         LoadIntvPort loadIntvPort
                                            ) {
        this.questionSetRepository = questionSetRepository;
        this.commonQuestionRepository = commonQuestionRepository;
        this.loadPreparationPort = loadPreparationPort;
        this.loadIntvPort = loadIntvPort;
    }

    // 맞춤 질문 생성 요청, DB 저장
    public void generateCustomQuestion(Long intvId) {
        PreparationForQuestionGeneration preparation =
                loadPreparationPort.loadByIntvId(intvId);
    }


    // 공통 질문 저장하기
    @Override
    public void saveQuestionSet(Long intvId, String content) {

        commonQuestionRepository.getActiveQuestions();
        loadIntvPort.loadById(intvId);
    }




    private Intv getOwnedIntv(Long userId, Long intvId) {
        Intv intv = loadIntvPort.loadById(intvId)
                .orElseThrow(() -> new IntvException(IntvErrorCode.INTV_NOT_FOUND));

        if (!intv.getUserId().equals(userId)) {
            throw new IntvException(IntvErrorCode.INTV_FORBIDDEN);
        }

        return intv;
    }

}
