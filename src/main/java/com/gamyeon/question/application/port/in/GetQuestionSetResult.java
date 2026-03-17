package com.gamyeon.question.application.port.in;

import com.gamyeon.question.domain.QuestionSet;
import java.util.List;

public record GetQuestionSetResult(Long intvId, List<QuestionSetItemResult> questions) {
  public static GetQuestionSetResult of(Long intvId, List<QuestionSet> questionSets) {
    return new GetQuestionSetResult(
        intvId, questionSets.stream().map(QuestionSetItemResult::from).toList());
  }
}
