package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlResult;
import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlUseCase;
import com.gamyeon.answer.domain.AnswerSuccessCode;
import com.gamyeon.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerController {

  private final IssueAnswerUploadUrlUseCase issueAnswerUploadUrlUseCase;
  @PostMapping("/api/v1/intvs/{questionSetId}/answers/presigned-url")
  public ResponseEntity<ApiResponse<AnswerUploadUrlResponse>> issueUploadUrl(
      Long userId,
      @PathVariable Long questionSetId,
      @Valid @RequestBody IssueAnswerUploadUrlRequest request) {
    userId = 1L;

    IssueAnswerUploadUrlResult result =
        issueAnswerUploadUrlUseCase.issueUploadUrl(request.toCommand(userId, questionSetId));

    return ApiResponse.success(
        AnswerSuccessCode.ANSWER_UPLOAD_URL_ISSUED, AnswerUploadUrlResponse.from(result));
  }
