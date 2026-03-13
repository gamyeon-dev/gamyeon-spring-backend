package com.gamyeon.question.application.port.out;

import com.gamyeon.preparation.domain.PreparationStatus;
import java.util.List;

public record PreparationForQuestionGeneration(
    Long preparationId, Long intvId, PreparationStatus status, List<PreparationSourceFile> files) {}
