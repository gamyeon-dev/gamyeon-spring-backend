package com.gamyeon.feedback.application.port.in;

import com.gamyeon.feedback.infrastructure.web.dto.FeedbackWebhookRequest;

public interface FeedbackWebhookUseCase {
  void handleWebhook(FeedbackWebhookRequest request);
}
