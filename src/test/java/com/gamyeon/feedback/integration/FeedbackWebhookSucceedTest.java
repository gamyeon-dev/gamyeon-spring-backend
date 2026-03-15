// src/test/java/com/gamyeon/feedback/integration/FeedbackWebhookSucceedTest.java
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
class FeedbackWebhookSucceedTest extends TestcontainersSupport {

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
        questionSetRepository.save(QuestionSetEntity.create(1L, "Redis 경험을 설명하세요.", 1)).getId();
  }

  @Test
  @DisplayName("SUCCEED Webhook 수신 시 FEEDBACKS에 SUCCEED 상태로 저장된다")
  void succeed_webhook_saves_feedback() throws Exception {
    Map<String, Object> body =
        Map.ofEntries(
            Map.entry("intv_question_id", savedQuestionSetId),
            Map.entry("status", "SUCCEED"),
            Map.entry("logic_score", 85),
            Map.entry("answer_composition_score", 70),
            Map.entry("reliability", 80),
            Map.entry("characteristic", "Redis 활용 경험을 수치로 명확히 설명함."),
            Map.entry("answer_summary", "Redis 캐싱으로 API 성능을 개선함."),
            Map.entry("strength", "구체적 수치를 근거로 제시했습니다."),
            Map.entry("improvement", "PREP 구조를 활용하면 전달력이 향상될 것입니다."),
            Map.entry("feedback_badges", List.of("수치 근거 활용", "경험 기반 답변")),
            Map.entry("gaze_score", 82),
            Map.entry("time_score", 91),
            Map.entry("answer_duration_ms", 54500),
            Map.entry("keyword_count", 2));

    mockMvc
        .perform(
            post("/internal/v1/feedbacks/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Internal-API-Key", "test-internal-api-key") // ← 추가
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value("FDBK-S000"));

    var feedbacks = feedbackJpaRepository.findAll();
    assertThat(feedbacks).hasSize(1);
    assertThat(feedbacks.get(0).getStatus()).isEqualTo(FeedbackStatus.SUCCEED);
  }
}
