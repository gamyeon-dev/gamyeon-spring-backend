package com.gamyeon.question.application.port.out;

import com.gamyeon.preparation.domain.PreparationStatus;
import java.util.List;

public record PreparationForQuestionGeneration(
    Long intvId, Long preparationId, PreparationStatus status, List<PreparationSourceFile> files) {}
