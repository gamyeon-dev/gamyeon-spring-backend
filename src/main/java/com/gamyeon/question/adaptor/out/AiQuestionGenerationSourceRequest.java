package com.gamyeon.question.adaptor.out;

import com.gamyeon.question.application.port.out.PreparationSourceFile;

public record AiQuestionGenerationSourceRequest(String fileType, String fileKey) {

  public static AiQuestionGenerationSourceRequest from(PreparationSourceFile file) {
    return new AiQuestionGenerationSourceRequest(file.fileType().name(), file.fileKey());
  }
}
