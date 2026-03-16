package com.gamyeon.report.application.port.out;

import com.gamyeon.answer.domain.Answer;
import java.util.List;
import java.util.Optional;

public interface LoadAnswerPort {
  Optional<Answer> findByQuestionSetId(Long questionSetId);

  List<Answer> findByIntvIdOrderByAnswerOrder(Long intvId);
}
