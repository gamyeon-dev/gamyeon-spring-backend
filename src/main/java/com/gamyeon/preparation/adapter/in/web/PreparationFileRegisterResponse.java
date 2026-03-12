package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.preparation.application.port.in.PreparationFileRegisterResult;
import com.gamyeon.preparation.domain.PreparationFileType;
import com.gamyeon.preparation.domain.PreparationStatus;

public record PreparationFileRegisterResponse(
        Long preparationId,
        Long fileId,
        PreparationFileType fileType,
        String originalFileName,
        String fileKey,
        String fileUrl,
        PreparationStatus preparationStatus
) {

    public static PreparationFileRegisterResponse from(PreparationFileRegisterResult result) {
        return new PreparationFileRegisterResponse(
                result.preparationId(),
                result.fileId(),
                result.fileType(),
                result.originalFileName(),
                result.fileKey(),
                result.fileUrl(),
                result.preparationStatus()
        );
    }
}
