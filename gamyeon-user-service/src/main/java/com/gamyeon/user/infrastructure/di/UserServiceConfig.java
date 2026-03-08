package com.gamyeon.user.infrastructure.di;

import com.gamyeon.user.application.service.NicknameResolver;
import com.gamyeon.user.infrastructure.jwt.JwtProperties;
import com.gamyeon.user.infrastructure.jwt.JwtProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class UserServiceConfig {

    @Bean
    public JwtProvider jwtProvider(JwtProperties jwtProperties) {
        return new JwtProvider(jwtProperties);
    }

    @Bean
    public NicknameResolver nicknameResolver() {
        return new NicknameResolver();
    }
}
