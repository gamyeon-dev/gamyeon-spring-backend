package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IssueAnswerUploadUrlRequest(
    @NotBlank String originalFileName,
    @NotBlank String contentType,
    @NotNull @Min(1) Long fileSizeBytes) {

  public IssueAnswerUploadUrlCommand toCommand(Long userId, Long questionSetId) {
    return new IssueAnswerUploadUrlCommand(
        userId, questionSetId, originalFileName, contentType, fileSizeBytes);
  }
}
