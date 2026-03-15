// src/main/java/com/gamyeon/feedback/domain/event/FeedbackSavedEvent.java
package com.gamyeon.feedback.domain.event;

import com.gamyeon.feedback.domain.FeedbackStatus;

public record FeedbackSavedEvent(Long intvId, Long questionSetId, FeedbackStatus status) {}
