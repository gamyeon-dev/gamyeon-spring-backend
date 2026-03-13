package com.gamyeon.intv;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gamyeon.common.exception.CommonErrorCode;
import com.gamyeon.intv.domain.IntvSuccessCode;
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
class IntvIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JwtProvider jwtProvider;

  @Test
  void 토큰_없이_인터뷰_생성시_401_반환() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/intvs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"백엔드면접\"}"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(CommonErrorCode.INVALID_TOKEN.getCode()));
  }

  @Test
  void 유효한_토큰으로_인터뷰_생성시_201_반환() throws Exception {
    String token = jwtProvider.createAccessToken(1L, "test@example.com");

    mockMvc
        .perform(
            post("/api/v1/intvs")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"백엔드면접\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.code").value(IntvSuccessCode.INTV_CREATED.getCode()))
        .andExpect(jsonPath("$.data.title").value("백엔드면접"));
  }

  @Test
  void 제목_누락시_400_반환() throws Exception {
    String token = jwtProvider.createAccessToken(1L, "test@example.com");

    mockMvc
        .perform(
            post("/api/v1/intvs")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 한글이_아닌_제목은_400_반환() throws Exception {
    String token = jwtProvider.createAccessToken(1L, "test@example.com");

    mockMvc
        .perform(
            post("/api/v1/intvs")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Backend Interview\"}"))
        .andExpect(status().isBadRequest());
  }
}
