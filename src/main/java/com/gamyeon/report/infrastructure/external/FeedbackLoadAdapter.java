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
    // 1. 해당 면접의 성공한 피드백 엔티티들을 가져옴
    List<FeedbackEntity> entities =
        feedbackJpaRepository.findAllByIntvIdAndStatus(intvId, FeedbackStatus.SUCCEED);

    // 2. [핵심] 정렬 기준이 되는 질문 ID 리스트를 생성 순(ID순)으로 정렬하여 가져옴
    // (QuestionSetRepository를 통해 해당 intvId의 질문 ID들만 가져온다고 가정)
    List<Long> sortedQuestionIds =
        entities.stream()
            .map(FeedbackEntity::getQuestionSetId)
            .distinct()
            .sorted() // ID 오름차순 정렬
            .toList();

    // 3. 각 피드백에 대해 정렬된 리스트에서의 순서(Index)를 찾아 부여
    return entities.stream()
        .map(
            entity -> {
              AiReportFeedbackItem item = convertToAiItem(entity);

              // 정렬된 리스트에서의 위치 + 1 이 곧 서비스상의 Index (1~7)
              int index = sortedQuestionIds.indexOf(entity.getQuestionSetId()) + 1;

              // DTO에 index 주입 (toBuilder 등을 활용)
              return item.toBuilder().index(index).build();
            })
        .toList();
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
