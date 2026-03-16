package com.gamyeon.answer.application.port.in;

import com.fasterxml.jackson.databind.JsonNode;

public record SendAnswerGazeSegmentCommand(Long userId, Long questionSetId, JsonNode body) {}
