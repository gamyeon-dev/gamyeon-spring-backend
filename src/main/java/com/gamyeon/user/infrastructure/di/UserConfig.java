package com.gamyeon.user.infrastructure.di;

import com.gamyeon.user.application.service.NicknameResolver;
import com.gamyeon.user.infrastructure.security.JwtProperties;
import com.gamyeon.user.infrastructure.security.JwtProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class UserConfig {

    @Bean
    public JwtProvider jwtProvider(JwtProperties jwtProperties) {
        return new JwtProvider(jwtProperties);
    }

    @Bean
    public NicknameResolver nicknameResolver() {
        return new NicknameResolver();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
