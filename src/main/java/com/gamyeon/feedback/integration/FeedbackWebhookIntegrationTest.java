package com.gamyeon.feedback.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.feedback.domain.FeedbackStatus;
import com.gamyeon.feedback.infrastructure.persistence.FeedbackJpaRepository;
import com.gamyeon.feedback.infrastructure.persistence.QuestionSetEntity;
import com.gamyeon.feedback.infrastructure.persistence.QuestionSetRepository;
import com.gamyeon.feedback.support.TestcontainersSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FeedbackWebhookIntegrationTest extends TestcontainersSupport {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired FeedbackJpaRepository feedbackJpaRepository;
    @Autowired QuestionSetRepository questionSetRepository;

    private Long savedQuestionSetId;

    @BeforeEach
    void setUp() {
        feedbackJpaRepository.deleteAll();
        questionSetRepository.deleteAll();

        QuestionSetEntity qs = QuestionSetEntity.create(1L, "Redis 경험을 설명하세요.", 1);
        savedQuestionSetId = questionSetRepository.save(qs).getId();
    }

    @Test
    @DisplayName("SUCCEED 상태 Webhook 수신 시 FEEDBACKS에 정상 저장된다")
    void succeed_webhook_saves_feedback() throws Exception {
        Map<String, Object> body = Map.of(
                "intv_question_id", savedQuestionSetId,
                "status", "SUCCEED",
                "logic_score", 85,
                "answer_composition_score", 70,
                "reliability", 80,
                "characteristic", "Redis 활용 경험을 수치로 명확히 설명함.",
                "answer_summary", "Redis 캐싱으로 API 성능을 개선함.",
                "strength", "구체적 수치를 근거로 제시했습니다.",
                "improvement", "PREP 구조를 활용하면 전달력이 향상될 것입니다.",
                "feedback_badges", List.of("수치 근거 활용", "경험 기반 답변"),
                "gaze_score", 82,
                "time_score", 91,
                "answer_duration_ms", 54500,
                "keyword_count", 2
        );

        mockMvc.perform(post("/internal/v1/feedbacks/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("FDBK-S000"));

        var feedbacks = feedbackJpaRepository.findAll();
        assertThat(feedbacks).hasSize(1);
        assertThat(feedbacks.get(0).getStatus()).isEqualTo(FeedbackStatus.SUCCEED);
    }

    @Test
    @DisplayName("FAILED 상태 Webhook 수신 시 FAILED로 저장된다")
    void failed_webhook_saves_feedback_as_failed() throws Exception {
        Map<String, Object> body = Map.of(
                "intv_question_id", savedQuestionSetId,
                "status", "FAILED",
                "gaze_score", 60,
                "time_score", 55,
                "answer_duration_ms", 3000,
                "keyword_count", 0,
                "reliability", 20,
                "characteristic", "",
                "feedback_badges", List.of()
        );

        mockMvc.perform(post("/internal/v1/feedbacks/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("FDBK-S000"));

        var feedbacks = feedbackJpaRepository.findAll();
        assertThat(feedbacks).hasSize(1);
        assertThat(feedbacks.get(0).getStatus()).isEqualTo(FeedbackStatus.FAILED);
    }

    @Test
    @DisplayName("존재하지 않는 questionSetId로 요청 시 404 반환")
    void unknown_question_set_returns_404() throws Exception {
        Map<String, Object> body = Map.of(
                "intv_question_id", 99999L,
                "status", "SUCCEED",
                "gaze_score", 80,
                "time_score", 80,
                "answer_duration_ms", 30000,
                "keyword_count", 1,
                "reliability", 70,
                "characteristic", "테스트",
                "feedback_badges", List.of()
        );

        mockMvc.perform(post("/internal/v1/feedbacks/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("FDBK-N001"));
    }
}
