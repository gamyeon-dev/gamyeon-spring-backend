package com.gamyeon.question.adaptor.in.dto.request;

import com.gamyeon.question.application.port.in.CreateQuestionSetCommand;
import java.util.List;

public record CallBackRequest(
    Long intvId, String status, List<String> questions, String errorMessage) {
  public CreateQuestionSetCommand toCommand() {
    return new CreateQuestionSetCommand(intvId, status, questions, errorMessage);
  }
}
