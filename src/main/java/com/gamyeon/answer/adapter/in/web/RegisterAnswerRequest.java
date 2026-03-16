package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.RegisterAnswerCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterAnswerRequest(
    @NotNull Long intvId,
    @NotBlank String originalFileName,
    @NotBlank String fileKey,
    @NotBlank String fileUrl,
    @NotBlank String contentType,
    @NotNull @Min(1) Long fileSizeBytes) {

  public RegisterAnswerCommand toCommand(Long userId, Long questionSetId) {
    return new RegisterAnswerCommand(
        userId,
        intvId,
        questionSetId,
        originalFileName,
        fileKey,
        fileUrl,
        contentType,
        fileSizeBytes);
  }
}
