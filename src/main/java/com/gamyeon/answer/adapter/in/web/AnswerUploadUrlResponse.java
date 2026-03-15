package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlResult;

public record AnswerUploadUrlResponse(
    Long questionSetId,
    String originalFileName,
    String fileKey,
    String presignedUrl,
    String fileUrl,
    long expiresInSeconds) {

  public static AnswerUploadUrlResponse from(IssueAnswerUploadUrlResult result) {
    return new AnswerUploadUrlResponse(
        result.questionSetId(),
        result.originalFileName(),
        result.fileKey(),
        result.presignedUrl(),
        result.fileUrl(),
        result.expiresInSeconds());
  }
}
