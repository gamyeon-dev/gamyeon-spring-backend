package com.gamyeon.question.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "question.generation")
public record QuestionGenerationProperties(String baseUrl) {}
