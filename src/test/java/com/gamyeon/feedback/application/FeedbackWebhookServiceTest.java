// src/test/java/com/gamyeon/feedback/application/FeedbackWebhookServiceTest.java
package com.gamyeon.feedback.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.feedback.application.exception.QuestionSetNotFoundException;
import com.gamyeon.feedback.application.service.FeedbackWebhookService;
import com.gamyeon.feedback.domain.FeedbackStatus;
import com.gamyeon.feedback.infrastructure.persistence.FeedbackPersistenceAdapter;
import com.gamyeon.feedback.infrastructure.persistence.QuestionSetRepository;
import com.gamyeon.feedback.infrastructure.web.dto.FeedbackWebhookRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedbackWebhookServiceTest {

  @Mock FeedbackPersistenceAdapter feedbackPersistence;
  @Mock QuestionSetRepository questionSetRepository;
  @Mock ObjectMapper objectMapper;

  @InjectMocks FeedbackWebhookService service;

  private FeedbackWebhookRequest succeedRequest;

  @BeforeEach
  void setUp() {
    succeedRequest =
        new FeedbackWebhookRequest(
            1L, "SUCCEED", 85, 70, 80, "특징", "요약", "잘한 점", "개선점", List.of("뱃지1"), 82, 91, 54500, 2);
  }

  @Test
  @DisplayName("정상 요청 시 save와 update가 각각 1회 호출된다")
  void handleWebhook_calls_save_and_update() throws Exception {
    given(questionSetRepository.findIntvIdById(1L)).willReturn(Optional.of(10L));
    given(objectMapper.writeValueAsString(any())).willReturn("{}");

    service.handleWebhook(succeedRequest);

    verify(feedbackPersistence, times(1)).save(any());
    verify(feedbackPersistence, times(1)).update(any());
  }

  @Test
  @DisplayName("존재하지 않는 questionSetId 요청 시 QuestionSetNotFoundException 발생")
  void handleWebhook_throws_when_question_set_not_found() {
    given(questionSetRepository.findIntvIdById(1L)).willReturn(Optional.empty());

    assertThatThrownBy(() -> service.handleWebhook(succeedRequest))
        .isInstanceOf(QuestionSetNotFoundException.class);

    verify(feedbackPersistence, never()).save(any());
  }

  @Test
  @DisplayName("FAILED 상태 요청 시 update에 FAILED 상태 도메인이 전달된다")
  void handleWebhook_failed_status() throws Exception {
    FeedbackWebhookRequest failedRequest =
        new FeedbackWebhookRequest(
            1L, "FAILED", null, null, 20, "", null, null, null, List.of(), 60, 55, 3000, 0);

    given(questionSetRepository.findIntvIdById(1L)).willReturn(Optional.of(10L));
    given(objectMapper.writeValueAsString(any())).willReturn("{}");

    service.handleWebhook(failedRequest);

    verify(feedbackPersistence, times(1))
        .update(argThat(f -> f.getStatus() == FeedbackStatus.FAILED));
  }
}
