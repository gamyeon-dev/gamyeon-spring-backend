package com.gamyeon.preparation.application.port.in;

import com.gamyeon.preparation.domain.PreparationFileType;

public record UploadPreparationFileUrlCommand(
        Long userId,
        Long intvId,
        PreparationFileType fileType,
        String originalFileName,
        String contentType,
        Long fileSizeBytes
) {
}
