package com.gamyeon.answer.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "answer.gaze")
public record AnswerGazeProperties(String baseUrl) {}
