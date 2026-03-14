// src/test/java/com/gamyeon/feedback/integration/FeedbackWebhookIdempotencyTest.java
package com.gamyeon.feedback.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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
class FeedbackWebhookIdempotencyTest extends TestcontainersSupport {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;
  @Autowired FeedbackJpaRepository feedbackJpaRepository;
  @Autowired QuestionSetRepository questionSetRepository;

  private Long savedQuestionSetId;
  private Map<String, Object> body;

  @BeforeEach
  void setUp() {
    feedbackJpaRepository.deleteAll();
    questionSetRepository.deleteAll();
    savedQuestionSetId =
        questionSetRepository.save(QuestionSetEntity.create(1L, "중복 테스트 질문", 1)).getId();

    body =
        Map.ofEntries(
            Map.entry("intv_question_id", savedQuestionSetId),
            Map.entry("status", "SUCCEED"),
            Map.entry("logic_score", 85),
            Map.entry("answer_composition_score", 70),
            Map.entry("reliability", 80),
            Map.entry("characteristic", "특징"),
            Map.entry("answer_summary", "요약"),
            Map.entry("strength", "잘한 점"),
            Map.entry("improvement", "개선점"),
            Map.entry("feedback_badges", List.of("뱃지1")),
            Map.entry("gaze_score", 82),
            Map.entry("time_score", 91),
            Map.entry("answer_duration_ms", 54500),
            Map.entry("keyword_count", 2));
  }

  @Test
  @DisplayName("동일 questionSetId로 중복 요청 시 FEEDBACKS 튜플이 1개만 생성된다")
  void duplicate_webhook_creates_only_one_feedback() throws Exception {
    String content = objectMapper.writeValueAsString(body);

    // 1차 요청
    mockMvc
        .perform(
            post("/internal/v1/feedbacks/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Internal-API-Key", "test-internal-api-key")
                .content(content))
        .andExpect(status().isOk());

    // 2차 요청 (재시도)
    mockMvc
        .perform(
            post("/internal/v1/feedbacks/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Internal-API-Key", "test-internal-api-key")
                .content(content))
        .andExpect(status().isOk());

    // FEEDBACKS 튜플은 1개만 존재해야 함
    assertThat(feedbackJpaRepository.findAll()).hasSize(1);
  }
}
