package com.gamyeon.dashboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gamyeon.dashboard.domain.Notice;
import com.gamyeon.dashboard.domain.NoticeCategory;
import com.gamyeon.dashboard.infrastructure.persistence.JpaNoticeRepository;
import com.gamyeon.user.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NoticeIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JpaNoticeRepository jpaNoticeRepository;

  @Autowired private JwtProvider jwtProvider;

  @BeforeEach
  void setUp() {
    jpaNoticeRepository.deleteAll();
  }

  @Test
  void 공지사항_목록_조회시_카테고리를_반환한다() throws Exception {
    Notice notice =
        jpaNoticeRepository.save(
            Notice.create("프론트엔드 직무 모의 면접 질문 세트가 추가되었습니다.", "content", NoticeCategory.UPDATE));
    String token = jwtProvider.createAccessToken(1L, "test@example.com");

    mockMvc
        .perform(get("/api/v1/notices").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data[0].id").value(notice.getId()))
        .andExpect(jsonPath("$.data[0].category").value(NoticeCategory.UPDATE.name()))
        .andExpect(jsonPath("$.data[0].title").value("프론트엔드 직무 모의 면접 질문 세트가 추가되었습니다."));
  }

  @Test
  void 공지사항_상세_조회시_카테고리를_반환한다() throws Exception {
    Notice notice =
        jpaNoticeRepository.save(Notice.create("서버 정기 점검 안내", "점검 내용", NoticeCategory.MAINTENANCE));
    String token = jwtProvider.createAccessToken(1L, "test@example.com");

    mockMvc
        .perform(
            get("/api/v1/notices/{noticeId}", notice.getId())
                .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(notice.getId()))
        .andExpect(jsonPath("$.data.category").value(NoticeCategory.MAINTENANCE.name()))
        .andExpect(jsonPath("$.data.title").value("서버 정기 점검 안내"))
        .andExpect(jsonPath("$.data.content").value("점검 내용"));
  }
}
