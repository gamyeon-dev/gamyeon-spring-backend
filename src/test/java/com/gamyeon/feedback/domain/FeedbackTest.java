// src/test/java/com/gamyeon/feedback/domain/FeedbackTest.java
package com.gamyeon.feedback.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedbackTest {

  @Test
  @DisplayName("createInProgress 호출 시 IN_PROGRESS 상태로 생성된다")
  void createInProgress_status() {
    Feedback feedback = Feedback.createInProgress(1L, 2L, "{\"status\":\"SUCCEED\"}");

    assertThat(feedback.getStatus()).isEqualTo(FeedbackStatus.IN_PROGRESS);
    assertThat(feedback.getIntvId()).isEqualTo(1L);
    assertThat(feedback.getQuestionSetId()).isEqualTo(2L);
    assertThat(feedback.getCreatedAt()).isNotNull();
  }

  @Test
  @DisplayName("complete 호출 시 상태가 SUCCEED로 변경된다")
  void complete_succeed() {
    Feedback feedback = Feedback.createInProgress(1L, 2L, "{}");

    feedback.complete(FeedbackStatus.SUCCEED, "{\"status\":\"SUCCEED\"}");

    assertThat(feedback.getStatus()).isEqualTo(FeedbackStatus.SUCCEED);
    assertThat(feedback.getUpdatedAt()).isNotNull();
  }

  @Test
  @DisplayName("complete 호출 시 상태가 FAILED로 변경된다")
  void complete_failed() {
    Feedback feedback = Feedback.createInProgress(1L, 2L, "{}");

    feedback.complete(FeedbackStatus.FAILED, "{}");

    assertThat(feedback.getStatus()).isEqualTo(FeedbackStatus.FAILED);
  }

  @Test
  @DisplayName("IN_PROGRESS가 아닌 상태에서 complete 호출 시 예외가 발생한다")
  void complete_throws_if_not_in_progress() {
    Feedback feedback = Feedback.createInProgress(1L, 2L, "{}");
    feedback.complete(FeedbackStatus.SUCCEED, "{}");

    assertThatThrownBy(() -> feedback.complete(FeedbackStatus.FAILED, "{}"))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("SKIPPED webhook status는 FAILED로 변환된다")
  void fromWebhook_skipped_to_failed() {
    assertThat(FeedbackStatus.fromWebhook("SKIPPED")).isEqualTo(FeedbackStatus.FAILED);
  }

  @Test
  @DisplayName("알 수 없는 webhook status는 예외가 발생한다")
  void fromWebhook_unknown_throws() {
    assertThatThrownBy(() -> FeedbackStatus.fromWebhook("UNKNOWN"))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
