// src/test/java/com/gamyeon/feedback/integration/FeedbackWebhookNotFoundTest.java
package com.gamyeon.feedback.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.feedback.support.TestcontainersSupport;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FeedbackWebhookNotFoundTest extends TestcontainersSupport {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @Test
  @DisplayName("존재하지 않는 questionSetId 요청 시 FDBK-N001 404 반환")
  void unknown_question_set_returns_404() throws Exception {
    Map<String, Object> body =
        Map.ofEntries(
            Map.entry("intv_question_id", 99999L),
            Map.entry("status", "SUCCEED"),
            Map.entry("reliability", 70),
            Map.entry("characteristic", "테스트"),
            Map.entry("feedback_badges", List.of()),
            Map.entry("gaze_score", 80),
            Map.entry("time_score", 80),
            Map.entry("answer_duration_ms", 30000),
            Map.entry("keyword_count", 1));

    mockMvc
        .perform(
            post("/internal/v1/feedbacks/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Internal-API-Key", "test-internal-api-key")
                .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("FDBK-N001"));
  }
}
