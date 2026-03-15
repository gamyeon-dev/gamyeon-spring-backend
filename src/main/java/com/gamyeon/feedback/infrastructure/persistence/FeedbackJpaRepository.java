package com.gamyeon.feedback.infrastructure.persistence;

import com.gamyeon.feedback.domain.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {

  boolean existsByQuestionSetIdAndStatusIn(
      Long questionSetId, java.util.List<FeedbackStatus> statuses);
}
