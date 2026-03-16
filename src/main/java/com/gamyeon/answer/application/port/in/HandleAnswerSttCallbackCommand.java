package com.gamyeon.answer.application.port.in;

public record HandleAnswerSttCallbackCommand(
    Long questionId, String correctedTranscript, String errorMessage) {}
