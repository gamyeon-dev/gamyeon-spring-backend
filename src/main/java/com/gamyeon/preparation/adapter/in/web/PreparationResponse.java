package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.preparation.application.port.in.UploadPreparationFileUrlResult;
import com.gamyeon.preparation.domain.PreparationFileType;

public record PreparationResponse(
    Long preparationId,
    PreparationFileType fileType,
    String originalFileName,
    String fileKey,
    String presignedUrl,
    String fileUrl,
    long expiresInSeconds) {

  public static PreparationResponse from(UploadPreparationFileUrlResult result) {
    return new PreparationResponse(
        result.preparationId(),
        result.fileType(),
        result.originalFileName(),
        result.fileKey(),
        result.presignedUrl(),
        result.fileUrl(),
        result.expiresInSeconds());
  }
}
