package com.gamyeon.question.adaptor.in.dto.response;

import com.gamyeon.question.application.port.in.GetQuestionSetResult;
import java.util.List;

public record QuestionSetResponse(Long intvId, List<QuestionSetItemResponse> questions) {
  public static QuestionSetResponse from(GetQuestionSetResult result) {
    return new QuestionSetResponse(
        result.intvId(), result.questions().stream().map(QuestionSetItemResponse::from).toList());
  }
}
