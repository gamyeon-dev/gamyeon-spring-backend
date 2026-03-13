package com.gamyeon.question.adaptor.in.dto.response;

import java.util.List;

public record QuestionSetResponse(Long intvId, List<GeneratedQuestionSetResponse> questions) {}
