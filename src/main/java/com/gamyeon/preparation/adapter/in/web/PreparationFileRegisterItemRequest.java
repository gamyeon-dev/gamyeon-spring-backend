package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.preparation.application.port.in.PreparationFileCommand;
import com.gamyeon.preparation.domain.PreparationFileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PreparationFileRegisterItemRequest(
    @NotNull(message = "파일 타입은 필수입니다.") PreparationFileType fileType,
    @NotBlank(message = "원본 파일명은 필수입니다.") String originalFileName,
    @NotBlank(message = "파일 키는 필수입니다.") String fileKey,
    @NotBlank(message = "파일 URL은 필수입니다.") String fileUrl) {

  public PreparationFileCommand toCommand(Long userId, Long intvId) {
    return new PreparationFileCommand(userId, intvId, fileType, originalFileName, fileKey, fileUrl);
  }
}
