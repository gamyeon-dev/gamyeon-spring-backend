package com.gamyeon.question.application.port.in;

import java.util.List;

public record CreateQuestionSetCommand(
    Long intvId, String status, List<String> questions, String errorMessage) {}
