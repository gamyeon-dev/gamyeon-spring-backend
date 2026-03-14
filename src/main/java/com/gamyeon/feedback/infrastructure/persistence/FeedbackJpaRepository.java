package com.gamyeon.feedback.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {}
