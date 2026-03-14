package com.gamyeon.question.adaptor.in.web;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.question.application.port.in.RequestCustomQuestionUseCase;
import com.gamyeon.question.domain.QuestionSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class QuestionSetsController {

  private final RequestCustomQuestionUseCase RequestCustomQuestionUseCase;

  @PostMapping("intvs/{intvId}/questions")
  public ResponseEntity<ApiResponse<Void>> createQuestionSet(
      Long userId, @PathVariable Long intvId) {
    userId = 1L;

    RequestCustomQuestionUseCase.create(userId, intvId);

    return ApiResponse.success(QuestionSuccessCode.REQUEST_SUCCESS);
  }
}
