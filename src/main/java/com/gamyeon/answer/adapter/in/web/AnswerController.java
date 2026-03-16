package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlResult;
import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlUseCase;
import com.gamyeon.answer.application.port.in.RegisterAnswerResult;
import com.gamyeon.answer.application.port.in.RegisterAnswerUseCase;
import com.gamyeon.answer.application.port.in.RequestAnswerAnalysisUseCase;
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
  private final RegisterAnswerUseCase registerAnswerUseCase;
  private final RequestAnswerAnalysisUseCase requestAnswerAnalysisUseCase;

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

  @PostMapping("/api/v1/intvs/{questionSetId}/answers")
  public ResponseEntity<ApiResponse<RegisterAnswerResponse>> registerAnswer(
      Long userId,
      @PathVariable Long questionSetId,
      @Valid @RequestBody RegisterAnswerRequest request) {
    userId = 1L;

    RegisterAnswerResult result =
        registerAnswerUseCase.register(request.toCommand(userId, questionSetId));

    return ApiResponse.success(
        AnswerSuccessCode.ANSWER_REGISTERED, RegisterAnswerResponse.from(result));
  }

  @PostMapping("/api/v1/answers/{answerId}/analysis")
  public ResponseEntity<ApiResponse<Void>> requestAnalysis(
      Long userId, @PathVariable Long answerId) {
    userId = 1L;

    requestAnswerAnalysisUseCase.requestAnalysis(
        new com.gamyeon.answer.application.port.in.RequestAnswerAnalysisCommand(userId, answerId));

    return ApiResponse.success(AnswerSuccessCode.ANSWER_ANALYSIS_REQUESTED);
  }
}
