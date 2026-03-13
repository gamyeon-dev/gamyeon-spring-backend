package com.gamyeon.question.adaptor.in.web;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.question.application.port.in.SaveQuestionSetUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/questions")
public class QuestionSetsController {

  private final SaveQuestionSetUseCase saveQuestionSetUseCase;

  public QuestionSetsController(SaveQuestionSetUseCase saveQuestionSetUseCase) {
    this.saveQuestionSetUseCase = saveQuestionSetUseCase;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveQuestionSet(
      @AuthenticationPrincipal Long userId, @Valid @RequestBody SaveQuestionSetRequest request) {
    saveQuestionSetUseCase.saveQuestionSet(request.intvId(), request.content());
    return ApiResponse.success(
        com.gamyeon.preparation.domain.PreparationSuccessCode.PREPARATION_FILE_REGISTERED);
  }

  public record SaveQuestionSetRequest(Long intvId, @NotBlank String content) {}
}
