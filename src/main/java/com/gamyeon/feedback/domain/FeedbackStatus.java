package com.gamyeon.feedback.domain;

public enum FeedbackStatus {
  IN_PROGRESS,
  SUCCEED,
  FAILED;

  /** Python Webhook의 status 문자열을 도메인 상태로 변환합니다. SKIPPED는 FAILED로 통합 처리합니다. */
  public static FeedbackStatus fromWebhook(String webhookStatus) {
    return switch (webhookStatus) {
      case "SUCCEED" -> SUCCEED;
      case "FAILED", "SKIPPED" -> FAILED;
      default -> throw new IllegalArgumentException("알 수 없는 webhook status: " + webhookStatus);
    };
  }
}
