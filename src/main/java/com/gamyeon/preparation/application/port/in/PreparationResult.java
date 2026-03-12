package com.gamyeon.preparation.application.port.in;

import com.gamyeon.preparation.domain.PreparationStatus;

import java.util.List;

public record PreparationResult(

        Long preparationId,
        Long intvId,
        PreparationStatus status,
        List<PreparationFileResult> files
) {
}
