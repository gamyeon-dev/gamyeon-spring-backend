package com.gamyeon.answer.infrastructure;

import com.gamyeon.answer.application.AnswerAnalysisProperties;
import com.gamyeon.answer.application.AnswerGazeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AnswerAnalysisProperties.class, AnswerGazeProperties.class})
public class AnswerConfig {}
