package com.gamyeon.feedback.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.feedback.application.exception.FeedbackSerializationException;
import com.gamyeon.feedback.application.exception.QuestionSetNotFoundException;
import com.gamyeon.feedback.application.port.in.FeedbackWebhookUseCase;
import com.gamyeon.feedback.domain.Feedback;
import com.gamyeon.feedback.domain.FeedbackStatus;
import com.gamyeon.feedback.infrastructure.persistence.FeedbackPersistenceAdapter;
import com.gamyeon.feedback.infrastructure.persistence.QuestionSetRepository;
import com.gamyeon.feedback.infrastructure.web.dto.FeedbackWebhookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackWebhookService implements FeedbackWebhookUseCase {

  private final FeedbackPersistenceAdapter feedbackPersistence;
  private final QuestionSetRepository questionSetRepository; // QUESTION_SETS 조회용
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public void handleWebhook(FeedbackWebhookRequest request) {
    Long questionSetId = request.intvQuestionId();

    // ① QUESTION_SETS → intv_id 추출
    Long intvId =
        questionSetRepository
            .findIntvIdById(questionSetId)
            .orElseThrow(() -> new QuestionSetNotFoundException(questionSetId));

    // ② 멱등성 처리 — 이미 완료된 요청은 무시
    boolean alreadyProcessed = feedbackPersistence.existsCompletedByQuestionSetId(questionSetId);
    if (alreadyProcessed) {
      log.info("[Feedback] 중복 요청 무시 | questionSetId={}", questionSetId);
      return;
    }

    // ③ Webhook Body 전체를 JSON 문자열로 직렬화
    String contentJson = serialize(request);

    // ④ IN_PROGRESS 상태로 FEEDBACKS 튜플 생성
    Feedback feedback = Feedback.createInProgress(intvId, questionSetId, contentJson);
    feedbackPersistence.save(feedback);
    log.info("[Feedback] IN_PROGRESS 저장 완료 | questionSetId={}", questionSetId);

    // ⑤ 최종 상태로 업데이트 (SUCCEED / FAILED)
    FeedbackStatus finalStatus = FeedbackStatus.fromWebhook(request.status());
    feedback.complete(finalStatus, contentJson);
    feedbackPersistence.update(feedback);
    log.info("[Feedback] {} 업데이트 완료 | id={}", finalStatus, feedback.getId());

    // ⑥ TODO: Report 모듈 이벤트 발행 (추후 구현)
    // eventPublisher.publish(new FeedbackCompletedEvent(intvId, questionSetId));
  }

  private String serialize(FeedbackWebhookRequest request) {
    try {
      return objectMapper.writeValueAsString(request);
    } catch (JsonProcessingException e) {
      throw new FeedbackSerializationException("Webhook body 직렬화 실패", e);
    }
  }
}
