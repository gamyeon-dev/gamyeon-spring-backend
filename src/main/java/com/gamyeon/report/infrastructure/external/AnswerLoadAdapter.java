package com.gamyeon.report.infrastructure.external;

import com.gamyeon.answer.domain.Answer;
import com.gamyeon.answer.domain.AnswerRepository;
import com.gamyeon.report.application.port.out.LoadAnswerPort;
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
    // intvId로 모든 답변 조회 후 answer_order 기준 정렬
    // AnswerRepository에 커스텀 쿼리 메서드가 없다면, findByIntvId를 사용하고 클라이언트에서 정렬
    return answerRepository.findByIntvId(intvId).stream()
        .sorted(
            (a1, a2) -> {
              // answer_order 필드가 없다면 questionSetId로 대체 정렬
              Integer order1 = extractOrderFromQuestionSetId(a1.getQuestionSetId());
              Integer order2 = extractOrderFromQuestionSetId(a2.getQuestionSetId());
              return order1.compareTo(order2);
            })
        .toList();
  }

  private Integer extractOrderFromQuestionSetId(Long questionSetId) {
    // questionSetId에서 answer_order 추출 (예: questionSetId % 1000 또는 별도 로직)
    return questionSetId.intValue() % 1000;
  }
}
