package com.gamyeon.user.infrastructure;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gamyeon.common.exception.CommonErrorCode;
import com.gamyeon.user.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JwtProvider jwtProvider;

  @Test
  void healthEndpointShouldReturnOk() throws Exception {
    mockMvc
        .perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.status").value("UP"));
  }

  @Test
  void usersMeShouldReturnUnauthorizedWithoutToken() throws Exception {
    mockMvc
        .perform(get("/api/v1/users/me"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(CommonErrorCode.INVALID_TOKEN.getCode()));
  }

  @Test
  void logoutShouldReturnUnauthorizedWithoutToken() throws Exception {
    mockMvc
        .perform(post("/api/v1/auth/logout"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(CommonErrorCode.INVALID_TOKEN.getCode()));
  }

  @Test
  void logoutShouldSucceedWithValidAccessToken() throws Exception {
    String accessToken = jwtProvider.createAccessToken(1L, "test@example.com");

    mockMvc
        .perform(post("/api/v1/auth/logout").header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  void internalEndpointShouldReturnForbiddenWithoutApiKey() throws Exception {
    mockMvc
        .perform(
            post("/api/internal/question/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.code").value(CommonErrorCode.FORBIDDEN.getCode()));
  }

  @Test
  void missingPathShouldReturnNotFound() throws Exception {
    mockMvc
        .perform(get("/actuator/missing-path"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(CommonErrorCode.NOT_FOUND.getCode()));
  }
}
