package com.gamyeon.preparation.application.port.in;

import com.gamyeon.preparation.domain.PreparationStatus;
import java.util.List;

public record PreparationFilesRegisterResult(
    Long preparationId,
    PreparationStatus preparationStatus,
    List<PreparationFileRegisterResult> files) {}
