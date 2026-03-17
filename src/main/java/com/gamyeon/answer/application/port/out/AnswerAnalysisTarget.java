package com.gamyeon.answer.application.port.out;

public record AnswerAnalysisTarget(
    Long intvId, Long questionSetId, String questionContent, String mediaFileKey) {}
