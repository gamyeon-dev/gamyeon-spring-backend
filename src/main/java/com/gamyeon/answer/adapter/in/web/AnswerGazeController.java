package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentUseCase;
import com.gamyeon.answer.domain.AnswerSuccessCode;
import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.common.security.CurrentUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerGazeController {

  private final SendAnswerGazeSegmentUseCase sendAnswerGazeSegmentUseCase;

  @PostMapping("/api/v1/intvs/{questionSetId}/gaze")
  public ResponseEntity<ApiResponse<Void>> sendGazeSegment(
      @CurrentUserId Long userId,
      @PathVariable Long questionSetId,
      @Valid @RequestBody AnswerGazeSegmentRequest request) {
    sendAnswerGazeSegmentUseCase.send(request.toCommand(userId, questionSetId));
    return ApiResponse.success(AnswerSuccessCode.ANSWER_GAZE_SEGMENT_RECEIVED);
  }
}
