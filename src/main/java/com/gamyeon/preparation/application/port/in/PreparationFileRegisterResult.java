package com.gamyeon.preparation.application.port.in;

import com.gamyeon.preparation.domain.PreparationFileType;
import com.gamyeon.preparation.domain.PreparationStatus;

public record PreparationFileRegisterResult(
        Long preparationId,
        Long fileId,
        PreparationFileType fileType,
        String originalFileName,
        String fileKey,
        String fileUrl,
        PreparationStatus preparationStatus
) {
}
