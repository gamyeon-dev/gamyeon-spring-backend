package com.gamyeon.question.application;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvErrorCode;
import com.gamyeon.intv.domain.IntvException;
import com.gamyeon.question.application.port.in.CreateQuestionSetCommand;
import com.gamyeon.question.application.port.in.CreateQuestionSetUseCase;
import com.gamyeon.question.application.port.in.RequestCustomQuestionUseCase;
import com.gamyeon.question.application.port.out.GenerateCustomQuestionPort;
import com.gamyeon.question.application.port.out.LoadIntvPort;
import com.gamyeon.question.application.port.out.LoadPreparationPort;
import com.gamyeon.question.application.port.out.PreparationForQuestionGeneration;
import com.gamyeon.question.domain.CommonQuestion;
import com.gamyeon.question.domain.CommonQuestionRepository;
import com.gamyeon.question.domain.QuestionSet;
import com.gamyeon.question.domain.QuestionSetRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionUseCaseSetApplicationService
    implements RequestCustomQuestionUseCase, CreateQuestionSetUseCase {

  private final QuestionSetRepository questionSetRepository;
  private final CommonQuestionRepository commonQuestionRepository;
  private final LoadPreparationPort loadPreparationPort;
  private final LoadIntvPort loadIntvPort;
  private final GenerateCustomQuestionPort generateCustomQuestionPort;

  // 어떤 intv에 저장할것인가?
  @Override
  public void create(Long userId, Long intvId) {

    getOwnedIntv(userId, intvId);
    PreparationForQuestionGeneration preparation = loadPreparationPort.loadByIntvId(intvId);
    generateCustomQuestionPort.request(preparation, "/internal/v1/questions/generate");
  }

  @Override
  public void create(CreateQuestionSetCommand command) {

    loadIntvPort
        .loadById(command.intvId())
        .orElseThrow(() -> new IntvException(IntvErrorCode.INTV_NOT_FOUND));

    // 맞춤 질문 0~4개 추출('FAIL' 이면 0으로 침)
    List<String> customQuestions;
    if (!Objects.equals(command.status(), "SUCCESS")) {
      customQuestions = List.of();
    } else {
      customQuestions = command.questions() == null ? List.of() : command.questions();
    }

    customQuestions = customQuestions.subList(0, Math.min(customQuestions.size(), 4));

    int commonQuestionCount = 7 - customQuestions.size(); // 가져올 공통 질문 개수 = 7 - 맞춤질문 개수

    List<String> commonQuestions =
        commonQuestionRepository.findRandomActiveQuestions(commonQuestionCount).stream()
            .map(CommonQuestion::getContent)
            .toList();

    List<String> finalQuestions = new ArrayList<>();
    finalQuestions.addAll(commonQuestions);
    finalQuestions.addAll(customQuestions);

    List<QuestionSet> questionSets =
        finalQuestions.stream()
            .map(content -> QuestionSet.create(command.intvId(), content))
            .toList();

    questionSetRepository.saveAll(questionSets);
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
