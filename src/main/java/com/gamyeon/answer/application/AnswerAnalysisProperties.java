package com.gamyeon.answer.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "answer.analysis")
public record AnswerAnalysisProperties(String baseUrl) {}
