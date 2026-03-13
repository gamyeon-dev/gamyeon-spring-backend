package com.gamyeon.preparation.application.port.in;

import com.gamyeon.preparation.domain.PreparationFileType;

public record UploadPreparationFileUrlResult(
    Long preparationId,
    PreparationFileType fileType,
    String originalFileName,
    String fileKey,
    String presignedUrl,
    String fileUrl,
    long expiresInSeconds) {}
