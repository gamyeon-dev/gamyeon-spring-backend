package com.gamyeon.preparation.application.port.in;

import com.gamyeon.preparation.domain.PreparationFileType;

public record PreparationFileCommand(
        PreparationFileType fileType,
        String originalFileName,
        String fileKey,
        String fileUrl
) {
}
