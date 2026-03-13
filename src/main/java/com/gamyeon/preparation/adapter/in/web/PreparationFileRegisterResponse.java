package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.preparation.application.port.in.PreparationFileRegisterResult;
import com.gamyeon.preparation.application.port.in.PreparationFilesRegisterResult;
import com.gamyeon.preparation.domain.PreparationFileType;
import com.gamyeon.preparation.domain.PreparationStatus;
import java.util.List;

public record PreparationFileRegisterResponse(
    Long preparationId,
    PreparationStatus preparationStatus,
    List<RegisteredPreparationFileResponse> files) {

  public static PreparationFileRegisterResponse from(PreparationFilesRegisterResult result) {
    return new PreparationFileRegisterResponse(
        result.preparationId(),
        result.preparationStatus(),
        result.files().stream().map(RegisteredPreparationFileResponse::from).toList());
  }

  public record RegisteredPreparationFileResponse(
      Long fileId,
      PreparationFileType fileType,
      String originalFileName,
      String fileKey,
      String fileUrl) {
    public static RegisteredPreparationFileResponse from(PreparationFileRegisterResult result) {
      return new RegisteredPreparationFileResponse(
          result.fileId(),
          result.fileType(),
          result.originalFileName(),
          result.fileKey(),
          result.fileUrl());
    }
  }
}
