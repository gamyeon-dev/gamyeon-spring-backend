package com.gamyeon.feedback.infrastructure.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackCallbackRequest {
  private Long intvId;
  private Long questionSetId;
  private String status; // SUCCEED | FAILED
  private Object content; // 피드백 결과 JSON 전체 (jsonb 저장용)
  private String errorMessage;
}
