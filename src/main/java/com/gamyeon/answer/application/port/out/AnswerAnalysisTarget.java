package com.gamyeon.answer.application.port.out;

public record AnswerAnalysisTarget(Long questionId, String fileKey, String questionContent) {}
