package com.gamyeon.report.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.feedback.support.TestcontainersSupport;
import com.gamyeon.report.domain.Report;
import com.gamyeon.report.domain.ReportStatus;
import com.gamyeon.report.infrastructure.persistence.ReportJpaRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReportWebhookControllerTest extends TestcontainersSupport {

  @Autowired private jakarta.persistence.EntityManager entityManager;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private ReportJpaRepository reportJpaRepository;

  @Test
  @DisplayName("AI 서버의 성공 콜백을 받으면 리포트 상태가 SUCCEED로 변경되고 리포트 데이터가 JSONB로 저장된다")
  void shouldUpdateReportToSucceedWhenCallbackReceived() throws Exception {
    // given
    Long intvId = 99L;
    reportJpaRepository.save(Report.createInProgress(intvId, 7L));

    Map<String, Object> callbackRequest =
        Map.of(
            "intvId",
            intvId,
            "status",
            "SUCCEED",
            "report",
            Map.of(
                "totalScore",
                85,
                "answeredCount",
                6, // ✅ NPE 방지를 위해 추가
                "strengths",
                List.of("논리적 사고"),
                "questionSummaries",
                List.of(Map.of("index", 1, "question", "자기소개", "feedbackBadges", List.of("열정적")))));

    // when
    mockMvc
        .perform(
            post("/internal/v1/reports/callback")
                .header("X-Internal-API-Key", "test-internal-api-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(callbackRequest)))
        .andExpect(status().isOk());

    // then
    entityManager.clear(); // 1차 캐시 초기화
    Report updatedReport = reportJpaRepository.findByIntvId(intvId).orElseThrow();
    assertThat(updatedReport.getStatus()).isEqualTo(ReportStatus.SUCCEED);
  }

  @Test
  @DisplayName("AI 콜백 JSON 명세 및 DB 저장 구조 확인 테스트")
  void verifyJsonSpecificationAndDatabaseStorage() throws Exception {
    // 1. 준비
    Long intvId = 123L;
    reportJpaRepository.saveAndFlush(Report.createInProgress(intvId, 7L));

    // 2. 입력 데이터 (모든 필수 필드 포함)
    Map<String, Object> callbackRequest =
        Map.of(
            "intvId",
            intvId,
            "status",
            "SUCCEED",
            "report",
            Map.of(
                "totalScore",
                92,
                "reportAccuracy",
                "HIGH",
                "answeredCount",
                7, // ✅ NPE 방지를 위해 추가
                "jobCategory",
                "Backend Developer",
                "competencyScores",
                Map.of("technical", 90),
                "strengths",
                List.of("논리적 사고"),
                "questionSummaries",
                List.of(Map.of("index", 1, "question", "질문", "feedbackBadges", List.of("뱃지")))));

    String requestJson =
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(callbackRequest);

    // 3. 실행
    mockMvc
        .perform(
            post("/internal/v1/reports/callback")
                .header("X-Internal-API-Key", "test-internal-api-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
        .andExpect(status().isOk());

    // 4. 검증
    entityManager.clear();
    Report savedReport = reportJpaRepository.findByIntvId(intvId).orElseThrow();
    assertThat(savedReport.getStatus()).isEqualTo(ReportStatus.SUCCEED);
  }

  @BeforeEach
  void setUp() {
    reportJpaRepository.deleteAll();
    // 만약 외래키 제약이 있다면 여기서 Interview 객체를 먼저 save해야 합니다.
  }

  @Test
  @DisplayName("AI 콜백 camelCase 명세 검증 및 NPE 해결 테스트")
  void debugReportCallbackSpecification() throws Exception {
    // 1. 준비: DB에 '진행 중' 리포트 생성
    Long targetIntvId = 123L;
    reportJpaRepository.saveAndFlush(Report.createInProgress(targetIntvId, 7L));

    // 2. [핵심] 모든 키값을 camelCase로 구성 (NPE 방지를 위해 answeredCount 포함)
    Map<String, Object> reportData = new HashMap<>();
    reportData.put("totalScore", 82);
    reportData.put("reportAccuracy", "높음");
    reportData.put("jobCategory", "Backend Developer");
    reportData.put("answeredCount", 6); // 👈 NPE 해결의 핵심 (Integer)
    reportData.put("avgAnswerDurationMs", 54500L);
    reportData.put("createdAt", "2026-03-16T23:30:00");
    reportData.put("competencyScores", Map.of("logic", 80, "gaze", 82));
    reportData.put("strengths", List.of("경험 기반 답변", "시간 관리 우수"));
    reportData.put("weaknesses", List.of("PREP 구조 활용 필요"));
    reportData.put(
        "questionSummaries",
        List.of(
            Map.of(
                "index",
                1,
                "question",
                "본인의 강점을 말해주세요",
                "answerSummary",
                "Redis 캐싱 경험 중심 답변",
                "feedbackBadges",
                List.of("경험 기반 답변"),
                "feedback",
                Map.of(
                    "characteristic", "핵심 개념 이해도 높음",
                    "strength", "실제 프로젝트 경험 강조",
                    "improvement", "구체적인 수치 보완 필요"))));

    Map<String, Object> requestMap =
        Map.of("intvId", targetIntvId, "userId", 7L, "status", "SUCCEED", "report", reportData);

    String jsonRequest =
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestMap);
    System.out.println("\n>>> [전송될 camelCase JSON 전문]\n" + jsonRequest);

    // 3. 실행: Webhook 호출
    mockMvc
        .perform(
            post("/internal/v1/reports/callback")
                .header("X-Internal-API-Key", "test-internal-api-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
        .andDo(print())
        .andExpect(status().isOk());

    // 4. 검증: 영속성 컨텍스트 비우고 DB 확인
    entityManager.clear();
    Report result = reportJpaRepository.findByIntvId(targetIntvId).orElseThrow();

    System.out.println(">>> [DB 저장 결과 상태]: " + result.getStatus());
    assertThat(result.getStatus()).isEqualTo(ReportStatus.SUCCEED);
    assertThat(result.getReportData()).isNotNull();
  }
}
