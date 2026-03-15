package com.gamyeon.answer.application.port.in;

public record RegisterAnswerCommand(
    Long userId,
    Long questionSetId,
    String originalFileName,
    String fileKey,
    String fileUrl,
    String contentType,
    Long fileSizeBytes) {}
