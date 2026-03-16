package com.gamyeon.answer.application.port.in;

public record IssueAnswerUploadUrlCommand(
    Long userId,
    Long questionSetId,
    String originalFileName,
    String contentType,
    Long fileSizeBytes) {}
