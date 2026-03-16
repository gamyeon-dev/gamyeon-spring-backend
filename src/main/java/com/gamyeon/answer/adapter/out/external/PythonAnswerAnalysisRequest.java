package com.gamyeon.answer.adapter.out.external;

import com.gamyeon.answer.application.port.out.AnswerAnalysisTarget;

public record PythonAnswerAnalysisRequest(Long questionId, String fileKey) {

  public static PythonAnswerAnalysisRequest from(AnswerAnalysisTarget target) {
    return new PythonAnswerAnalysisRequest(target.questionId(), target.fileKey());
  }
}
