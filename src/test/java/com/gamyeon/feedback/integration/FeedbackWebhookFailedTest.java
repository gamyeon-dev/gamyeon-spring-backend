// src/test/java/com/gamyeon/feedback/integration/FeedbackWebhookFailedTest.java
package com.gamyeon.feedback.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.feedback.domain.FeedbackStatus;
import com.gamyeon.feedback.infrastructure.persistence.FeedbackJpaRepository;
import com.gamyeon.feedback.infrastructure.persistence.QuestionSetEntity;
import com.gamyeon.feedback.infrastructure.persistence.QuestionSetRepository;
import com.gamyeon.feedback.support.TestcontainersSupport;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FeedbackWebhookFailedTest extends TestcontainersSupport {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;
  @Autowired FeedbackJpaRepository feedbackJpaRepository;
  @Autowired QuestionSetRepository questionSetRepository;

  private Long savedQuestionSetId;

  @BeforeEach
  void setUp() {
    feedbackJpaRepository.deleteAll();
    questionSetRepository.deleteAll();
    savedQuestionSetId =
        questionSetRepository.save(QuestionSetEntity.create(1L, "자기소개를 해주세요.", 1)).getId();
  }

  @Test
  @DisplayName("FAILED Webhook 수신 시 FEEDBACKS에 FAILED 상태로 저장된다")
  void failed_webhook_saves_feedback_as_failed() throws Exception {
    Map<String, Object> body =
        Map.ofEntries(
            Map.entry("intv_question_id", savedQuestionSetId),
            Map.entry("status", "FAILED"),
            Map.entry("reliability", 20),
            Map.entry("characteristic", ""),
            Map.entry("feedback_badges", List.of()),
            Map.entry("gaze_score", 60),
            Map.entry("time_score", 55),
            Map.entry("answer_duration_ms", 3000),
            Map.entry("keyword_count", 0));

    mockMvc
        .perform(
            post("/internal/v1/feedbacks/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Internal-API-Key", "test-internal-api-key")
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("FDBK-S000"));

    var feedbacks = feedbackJpaRepository.findAll();
    assertThat(feedbacks).hasSize(1);
    assertThat(feedbacks.get(0).getStatus()).isEqualTo(FeedbackStatus.FAILED);
  }
}
