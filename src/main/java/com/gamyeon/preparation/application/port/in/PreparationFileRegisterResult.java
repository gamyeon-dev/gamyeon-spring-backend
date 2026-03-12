package com.gamyeon.preparation.application.port.in;

import com.gamyeon.preparation.domain.PreparationStatus;

import java.util.List;

public record PreparationFileRegisterResult(
        Long preparationId,
        PreparationStatus preparationStatus,
        List<PreparationFileResult> files
) {
}
