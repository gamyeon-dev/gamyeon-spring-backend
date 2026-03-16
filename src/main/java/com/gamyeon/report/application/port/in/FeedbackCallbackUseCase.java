package com.gamyeon.feedback.application.port.in;

import com.gamyeon.feedback.infrastructure.web.dto.FeedbackCallbackRequest;

public interface FeedbackCallbackUseCase {
  /** AI 서버로부터 수신한 문항별 피드백 결과를 처리하고 FeedbackSavedEvent를 발행합니다. */
  void processFeedbackCallback(FeedbackCallbackRequest request);
}
