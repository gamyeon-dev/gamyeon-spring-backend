package com.gamyeon.question.infrastuctur;

import com.gamyeon.question.application.QuestionGenerationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.gamyeon.question.adaptor.out")
@EnableConfigurationProperties(QuestionGenerationProperties.class)
public class QuestionConfig {}
