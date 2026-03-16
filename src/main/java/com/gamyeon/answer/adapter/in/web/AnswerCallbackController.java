package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.HandleAnswerSttCallbackUseCase;
import com.gamyeon.answer.domain.AnswerSuccessCode;
import com.gamyeon.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerCallbackController {

  private final HandleAnswerSttCallbackUseCase handleAnswerSttCallbackUseCase;

  @PostMapping("/internal/v1/answers/callback")
  public ResponseEntity<ApiResponse<Void>> handleSttCallback(
      @Valid @RequestBody AnswerSttCallbackRequest request) {
    handleAnswerSttCallbackUseCase.handle(request.toCommand());
    return ApiResponse.success(AnswerSuccessCode.ANSWER_STT_CALLBACK_PROCESSED);
  }
}
