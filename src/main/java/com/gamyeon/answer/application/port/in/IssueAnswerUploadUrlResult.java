package com.gamyeon.answer.application.port.in;

public record IssueAnswerUploadUrlResult(
    Long questionSetId,
    String originalFileName,
    String fileKey,
    String presignedUrl,
    String fileUrl,
    long expiresInSeconds) {}
