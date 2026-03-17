package com.gamyeon.report.infrastructure.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.feedback.infrastructure.persistence.FeedbackJpaRepository;
import com.gamyeon.report.application.port.out.LoadReportPort;
import com.gamyeon.report.application.port.out.SaveReportPort;
import com.gamyeon.report.domain.Report;
import com.gamyeon.report.domain.ReportStatus;
import com.gamyeon.report.infrastructure.web.dto.ReportListResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportPersistenceAdapter implements LoadReportPort, SaveReportPort {

  private final ReportJpaRepository repository;
  private final FeedbackJpaRepository feedbackJpaRepository;
  private final ObjectMapper objectMapper; // JSON 파싱용

  // ── [SaveReportPort 구현] ──────────────────────────────────────────────────

  @Override
  public Report save(Report report) {
    return repository.save(report);
  }

  // ── [LoadReportPort 구현] ──────────────────────────────────────────────────

  @Override
  public boolean existsByIntvId(Long intvId) {
    return repository.existsByIntvId(intvId);
  }

  @Override
  public Optional<Report> findByIntvId(Long intvId) {
    return repository.findByIntvId(intvId);
  }

  @Override
  public Optional<Report> findByIntvIdWithLock(Long intvId) {
    return repository.findByIntvIdWithLock(intvId);
  }

  @Override
  public List<Report> findAllTimedOut(ReportStatus status, LocalDateTime threshold) {
    return repository.findAllByStatusAndCreatedAtBefore(status, threshold);
  }

  @Override
  public List<Report> findAllByUserId(Long userId) {
    return repository.findAllByUserId(userId);
  }

  // delete 구현
  @Override
  public void delete(Report report) {
    repository.delete(report);
  }

  @Override
  public List<ReportListResponse> findAllByUserIdWithInterviewInfo(Long userId) {
    // 1. 레포지토리에서 Native Query 결과(Mapping 인터페이스) 가져오기
    List<ReportListMapping> results = repository.findAllByUserIdWithInterviewInfo(userId);

    // 2. Mapping 결과를 DTO로 변환하여 반환
    return results.stream()
        .map(
            m -> {
              // 리포트가 있는 경우에만 요약 정보 생성
              ReportListResponse.ReportSummary summary = null;
              if (m.getReportStatus() != null) {
                summary =
                    ReportListResponse.ReportSummary.builder()
                        .reportId(m.getReportId())
                        .reportStatus(m.getReportStatus())
                        .totalScore(m.getTotalScore())
                        .answeredCount(m.getAnsweredCount())
                        .build();
              }

              return ReportListResponse.builder()
                  .intvId(m.getIntvId())
                  .intvTitle(m.getIntvTitle())
                  .intvStatus(m.getIntvStatus())
                  .durationMs(m.getDurationMs())
                  .updatedAt(m.getUpdatedAt())
                  .report(summary)
                  .build();
            })
        .toList();
  }
}
