package com.gamyeon.answer.application.port.out;

import com.fasterxml.jackson.databind.JsonNode;

public record AnswerGazeSegmentPayload(Long questionSetId, JsonNode body) {}
