package com.gamyeon.report.application.service;

import com.gamyeon.report.application.exception.ReportNotFoundException;
import com.gamyeon.report.application.port.in.DeleteReportUseCase;
import com.gamyeon.report.application.port.in.GetReportDetailUseCase;
import com.gamyeon.report.application.port.in.GetReportListUseCase;
import com.gamyeon.report.application.port.out.LoadReportPort;
import com.gamyeon.report.application.port.out.SaveReportPort;
import com.gamyeon.report.domain.Report;
import com.gamyeon.report.infrastructure.web.dto.ReportDetailResponse;
import com.gamyeon.report.infrastructure.web.dto.ReportListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportQueryService
    implements GetReportListUseCase, GetReportDetailUseCase, DeleteReportUseCase {

  private final LoadReportPort loadReportPort;
  private final SaveReportPort saveReportPort;

  // ── 목록 조회 ──────────────────────────────────────────────────────────────

  @Override
  @Transactional(readOnly = true)
  public List<ReportListResponse> getList(Long userId) {
    List<Report> reports = loadReportPort.findAllByUserId(userId);
    return reports.stream()
        .map(
            report ->
                ReportListResponse.of(
                    report.getIntvId(),
                    null, // TODO: INTV 조인 후 intvTitle 채우기
                    null, // TODO: INTV 조인 후 intvStatus 채우기
                    null, // TODO: INTV 조인 후 durationMs 채우기
                    report.getUpdatedAt(),
                    report))
        .toList();
  }

  // ── 상세 조회 ──────────────────────────────────────────────────────────────

  @Override
  @Transactional(readOnly = true)
  public ReportDetailResponse getDetail(Long intvId, Long userId) {
    Report report =
        loadReportPort.findByIntvId(intvId).orElseThrow(() -> new ReportNotFoundException(intvId));

    return ReportDetailResponse.builder()
        .interviewId(report.getIntvId())
        .interviewStatus(null) // TODO: INTV 조인 후 채우기
        .reportStatus(report.getStatus().name())
        .report(report.getStatus().name().equals("SUCCEED") ? parseReportData(report) : null)
        .build();
  }

  // ── 삭제 ──────────────────────────────────────────────────────────────────

  @Override
  @Transactional
  public void delete(Long intvId, Long userId) {
    Report report =
        loadReportPort.findByIntvId(intvId).orElseThrow(() -> new ReportNotFoundException(intvId));

    // 본인 소유 검증
    if (!report.getUserId().equals(userId)) {
      throw new ReportNotFoundException(intvId); // 타인 리포트는 없는 것처럼 처리
    }

    saveReportPort.delete(report);
  }

  // ── report_data jsonb 파싱 ─────────────────────────────────────────────────

  @SuppressWarnings("unchecked")
  private ReportDetailResponse.ReportDetail parseReportData(Report report) {
    if (report.getReportData() == null) return null;

    // report_data는 AiReportCallbackRequest.ReportData 타입으로 저장되어 있음
    // Jackson이 jsonb → Object로 역직렬화 시 LinkedHashMap으로 반환
    // ReportDetailResponse.ReportDetail에 직접 매핑
    try {
      java.util.Map<String, Object> data = (java.util.Map<String, Object>) report.getReportData();

      // questionSummaries: Map → QuestionSummary 변환
      List<ReportDetailResponse.QuestionSummary> questionSummaries = null;
      Object rawSummaries = data.get("questionSummaries");
      if (rawSummaries instanceof List<?> rawList) {
        questionSummaries =
            rawList.stream()
                .filter(item -> item instanceof java.util.Map)
                .map(
                    item -> {
                      java.util.Map<String, Object> map = (java.util.Map<String, Object>) item;
                      Object rawFeedback = map.get("feedback");
                      ReportDetailResponse.QuestionFeedback feedback = null;
                      if (rawFeedback instanceof java.util.Map<?, ?> fb) {
                        java.util.Map<String, Object> fbMap = (java.util.Map<String, Object>) fb;
                        feedback =
                            ReportDetailResponse.QuestionFeedback.builder()
                                .characteristic((String) fbMap.get("characteristic"))
                                .strength((String) fbMap.get("strength"))
                                .improvement((String) fbMap.get("improvement"))
                                .build();
                      }
                      return ReportDetailResponse.QuestionSummary.builder()
                          .index((Integer) map.get("index"))
                          .question((String) map.get("question"))
                          .answerSummary((String) map.get("answerSummary"))
                          .feedbackBadges((List<String>) map.get("feedbackBadges"))
                          .feedback(feedback)
                          .build();
                    })
                .toList();
      }

      return ReportDetailResponse.ReportDetail.builder()
          .totalScore((Integer) data.get("totalScore"))
          .reportAccuracy((String) data.get("reportAccuracy"))
          .jobCategory((String) data.get("jobCategory"))
          .answeredCount((Integer) data.get("answeredCount"))
          .avgAnswerDurationMs(
              data.get("avgAnswerDurationMs") != null
                  ? Long.valueOf(data.get("avgAnswerDurationMs").toString())
                  : null)
          .createdAt(null)
          .competencyScores((java.util.Map<String, Integer>) data.get("competencyScores"))
          .strengths((List<String>) data.get("strengths"))
          .weaknesses((List<String>) data.get("weaknesses"))
          .questionSummaries(questionSummaries)
          .build();

    } catch (Exception e) {
      log.error("[Report] report_data 파싱 실패 - intvId={}", report.getIntvId(), e);
      return null;
    }
  }
}
