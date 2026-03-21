package com.gamyeon.user.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.user.application.port.outbound.BlacklistedAccessTokenRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtProvider jwtProvider;
  private final BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;
  private final ObjectMapper objectMapper;
  private final String internalApiKey;

  public SecurityConfig(
      JwtProvider jwtProvider,
      BlacklistedAccessTokenRepository blacklistedAccessTokenRepository,
      ObjectMapper objectMapper,
      @Value("${internal.api-key}") String internalApiKey) {
    this.jwtProvider = jwtProvider;
    this.blacklistedAccessTokenRepository = blacklistedAccessTokenRepository;
    this.objectMapper = objectMapper;
    this.internalApiKey = internalApiKey;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/v1/auth/login/**", "/api/v1/auth/reissue")
                    .permitAll()
                    .requestMatchers("/internal/**")
                    .permitAll()
                    .requestMatchers("/health", "/actuator/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtProvider, blacklistedAccessTokenRepository, objectMapper);
  }

  @Bean
  public InternalApiKeyFilter internalApiKeyFilter() {
    return new InternalApiKeyFilter(internalApiKey, objectMapper);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(List.of("*"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
