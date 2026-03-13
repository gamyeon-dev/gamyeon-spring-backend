package com.gamyeon.question.application;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvErrorCode;
import com.gamyeon.intv.domain.IntvException;
import com.gamyeon.question.application.port.in.RequestForCustomQuestion;
import com.gamyeon.question.application.port.out.GenerateCustomQuestionPort;
import com.gamyeon.question.application.port.out.LoadIntvPort;
import com.gamyeon.question.application.port.out.LoadPreparationPort;
import com.gamyeon.question.application.port.out.PreparationForQuestionGeneration;
import com.gamyeon.question.domain.CommonQuestionRepository;
import com.gamyeon.question.domain.QuestionSetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionSetApplicationService implements RequestForCustomQuestion {

  private final QuestionSetRepository questionSetRepository;
  private final CommonQuestionRepository commonQuestionRepository;

  private final LoadPreparationPort loadPreparationPort;
  private final LoadIntvPort loadIntvPort;
  private final GenerateCustomQuestionPort generateCustomQuestionPort;

  public QuestionSetApplicationService(
      QuestionSetRepository questionSetRepository,
      CommonQuestionRepository commonQuestionRepository,
      LoadPreparationPort loadPreparationPort,
      LoadIntvPort loadIntvPort,
      GenerateCustomQuestionPort generateCustomQuestionPort) {
    this.questionSetRepository = questionSetRepository;
    this.commonQuestionRepository = commonQuestionRepository;
    this.loadPreparationPort = loadPreparationPort;
    this.loadIntvPort = loadIntvPort;
    this.generateCustomQuestionPort = generateCustomQuestionPort;
  }

  // 어떤 intv에 저장할것인가?
  @Override
  public void create(Long userId, Long intvId) {

    getOwnedIntv(userId, intvId);
    PreparationForQuestionGeneration preparation = loadPreparationPort.loadByIntvId(intvId);
    generateCustomQuestionPort.request(preparation, "/internal/v1/questions/generate");
  }

  private Intv getOwnedIntv(Long userId, Long intvId) {
    Intv intv =
        loadIntvPort
            .loadById(intvId)
            .orElseThrow(() -> new IntvException(IntvErrorCode.INTV_NOT_FOUND));

    if (!intv.getUserId().equals(userId)) {
      throw new IntvException(IntvErrorCode.INTV_FORBIDDEN);
    }

    return intv;
  }
}
