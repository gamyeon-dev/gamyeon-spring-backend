package com.gamyeon.report.infrastructure.external;

import com.gamyeon.answer.domain.Answer;
import com.gamyeon.answer.domain.AnswerRepository;
import com.gamyeon.report.application.port.out.LoadAnswerPort;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerLoadAdapter implements LoadAnswerPort {

  private final AnswerRepository answerRepository;

  @Override
  public Optional<Answer> findByQuestionSetId(Long questionSetId) {
    return answerRepository.findByQuestionSetId(questionSetId);
  }

  @Override
  public List<Answer> findByIntvIdOrderByAnswerOrder(Long intvId) {
    List<Answer> answers = answerRepository.findByIntvId(intvId);

    // questionSetId(질문 생성 순서)를 기준으로 정렬하여
    // 실제 면접 진행 순서와 일치시킴
    return answers.stream().sorted(Comparator.comparing(Answer::getQuestionSetId)).toList();
  }

  private Integer extractOrderFromQuestionSetId(Long questionSetId) {
    // questionSetId에서 answer_order 추출 (예: questionSetId % 1000 또는 별도 로직)
    return questionSetId.intValue() % 1000;
  }
}
