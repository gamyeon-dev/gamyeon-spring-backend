package com.gamyeon.answer.adapter.out.external;

import com.gamyeon.answer.application.port.out.AnswerAnalysisTarget;

public record PythonAnswerAnalysisRequest(
    Long intvId, Long questionSetId, String questionContent, String mediaFileKey) {

  public static PythonAnswerAnalysisRequest from(AnswerAnalysisTarget target) {
    return new PythonAnswerAnalysisRequest(
        target.intvId(), target.questionSetId(), target.questionContent(), target.mediaFileKey());
  }
}
