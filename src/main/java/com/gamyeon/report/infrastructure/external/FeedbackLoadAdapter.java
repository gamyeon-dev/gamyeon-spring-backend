package com.gamyeon.report.infrastructure.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.feedback.domain.FeedbackStatus;
import com.gamyeon.feedback.infrastructure.persistence.FeedbackEntity;
import com.gamyeon.feedback.infrastructure.persistence.FeedbackJpaRepository;
import com.gamyeon.report.application.port.out.LoadFeedbackPort;
import com.gamyeon.report.infrastructure.external.dto.AiReportFeedbackItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedbackLoadAdapter implements LoadFeedbackPort {

  private final FeedbackJpaRepository feedbackJpaRepository;
  private final ObjectMapper objectMapper;

  @Override
  public int countSucceedByIntvId(Long intvId) {
    return feedbackJpaRepository.countByIntvIdAndStatus(intvId, FeedbackStatus.SUCCEED);
  }

  @Override
  public List<AiReportFeedbackItem> findSucceedFeedbacksByIntvId(Long intvId) {
    List<FeedbackEntity> entities =
        feedbackJpaRepository.findAllByIntvIdAndStatus(intvId, FeedbackStatus.SUCCEED);

    return entities.stream().map(this::convertToAiItem).toList();
  }

  private AiReportFeedbackItem convertToAiItem(FeedbackEntity entity) {
    try {
      AiReportFeedbackItem item =
          objectMapper.readValue(entity.getContent(), AiReportFeedbackItem.class);

      return item.toBuilder()
          .questionSetId(entity.getQuestionSetId())
          .status(entity.getStatus().name())
          .build();

    } catch (JsonProcessingException e) {
      log.error(
          "[FeedbackLoad] JSON 파싱 실패 - feedbackId={}, intvId={}",
          entity.getId(),
          entity.getIntvId());
      throw new RuntimeException("피드백 데이터 변환 중 오류가 발생했습니다.", e);
    }
  }
}
