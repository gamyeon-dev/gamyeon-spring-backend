package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.preparation.application.port.in.UploadPreparationFileUrlCommand;
import com.gamyeon.preparation.domain.PreparationFileType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PreparationRequest(
    @NotNull(message = "파일 타입은 필수입니다.") PreparationFileType fileType,
    @NotBlank(message = "원본 파일명은 필수입니다.") String originalFileName,
    @NotBlank(message = "contentType은 필수입니다.") String contentType,
    @NotNull(message = "파일 크기는 필수입니다.") @Min(value = 1, message = "파일 크기는 1바이트 이상이어야 합니다.")
        Long fileSizeBytes) {

  public UploadPreparationFileUrlCommand toCommand(Long userId, Long intvId) {
    return new UploadPreparationFileUrlCommand(
        userId, intvId, fileType, originalFileName, contentType, fileSizeBytes);
  }
}
