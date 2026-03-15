package com.gamyeon.feedback.infrastructure.persistence;

import com.gamyeon.feedback.domain.Feedback;
import com.gamyeon.feedback.domain.FeedbackStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedbackPersistenceAdapter {

  private final FeedbackJpaRepository jpaRepository;

  public void save(Feedback domain) {
    FeedbackEntity saved = jpaRepository.save(FeedbackEntity.fromDomain(domain));
    domain.assignId(saved.getId()); // DB 생성 ID를 도메인 객체에 반영
  }

  public void update(Feedback domain) {
    FeedbackEntity entity =
        jpaRepository
            .findById(domain.getId())
            .orElseThrow(() -> new IllegalStateException("업데이트 대상 Feedback 없음: " + domain.getId()));
    FeedbackEntity updated = FeedbackEntity.fromDomain(domain);
    jpaRepository.save(updated);
  }

  public boolean existsCompletedByQuestionSetId(Long questionSetId) {
    return jpaRepository.existsByQuestionSetIdAndStatusIn(
        questionSetId, List.of(FeedbackStatus.SUCCEED, FeedbackStatus.FAILED));
  }
}
