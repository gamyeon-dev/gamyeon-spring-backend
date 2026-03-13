package com.gamyeon.question.adaptor.in.web;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.question.application.port.in.RequestForCustomQuestion;
import com.gamyeon.question.domain.QuestionSuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class QuestionSetsController {

  private final RequestForCustomQuestion RequestForCustomQuestion;

  public QuestionSetsController(RequestForCustomQuestion RequestForCustomQuestion) {
    this.RequestForCustomQuestion = RequestForCustomQuestion;
  }

  @PostMapping("intvs/{intvId}/questions")
  public ResponseEntity<ApiResponse<Void>> createQuestionSet(
      Long userId, @PathVariable Long intvId) {
    userId = 1L;

    RequestForCustomQuestion.create(userId, intvId);

    return ApiResponse.success(QuestionSuccessCode.REQUEST_SUCCESS);
  }
}
