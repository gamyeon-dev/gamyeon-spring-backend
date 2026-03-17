package com.gamyeon.question.infrastucture;

import com.gamyeon.question.application.QuestionGenerationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(QuestionGenerationProperties.class)
public class QuestionConfig {}
