package com.gamyeon.question.adaptor.out;

import com.gamyeon.question.application.port.out.PreparationForQuestionGeneration;
import java.util.List;

public record AiQuestionGenerationRequest(
    Long intvId, List<AiQuestionGenerationSourceRequest> files, String callback) {

  public static AiQuestionGenerationRequest from(
      PreparationForQuestionGeneration preparation, String callback) {
    return new AiQuestionGenerationRequest(
        preparation.intvId(),
        preparation.files().stream().map(AiQuestionGenerationSourceRequest::from).toList(),
        callback);
  }
}
