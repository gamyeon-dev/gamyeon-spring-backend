// package com.gamyeon.report.application.service;
//
// import com.gamyeon.report.application.port.in.GetReportListUseCase;
// import com.gamyeon.report.application.port.out.LoadReportPort;
// import com.gamyeon.report.infrastructure.persistence.ReportJpaRepository;
// import com.gamyeon.report.infrastructure.persistence.ReportListMapping;
// import com.gamyeon.report.infrastructure.web.dto.ReportListResponse;
// import java.util.List;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// @Service
// @RequiredArgsConstructor
// @Transactional(readOnly = true)
// public class GetReportListService implements GetReportListUseCase {
//
//  private final LoadReportPort loadReportPort;
//  private final ReportJpaRepository reportJpaRepository;
//
//  @Override
//  public List<ReportListResponse> getList(Long userId) {
//    // LoadReportPort 구현체(Infrastructure)에서
//    // SELECT * FROM interviews i LEFT JOIN reports r ON i.id = r.intv_id ...
//    // 와 같은 쿼리가 실행되어야 합니다.
//    List<ReportListMapping> results =
// reportJpaRepository.findAllByUserIdWithInterviewInfo(userId);
//    return results.stream()
//        .map(
//            m -> {
//              // 1. 내부 ReportSummary 객체 먼저 생성 (리포트가 있을 때만)
//              ReportListResponse.ReportSummary summary = null;
//              if (m.getReportStatus() != null) {
//                summary =
//                    ReportListResponse.ReportSummary.builder()
//                        .reportId(m.getReportId())
//                        .reportStatus(m.getReportStatus())
//                        .totalScore(m.getTotalScore())
//                        .answeredCount(m.getAnsweredCount())
//                        .build();
//              }
//
//              // 2. 최상위 DTO 조립
//              return ReportListResponse.builder()
//                  .intvId(m.getIntvId())
//                  .intvTitle(m.getIntvTitle())
//                  .intvStatus(m.getIntvStatus())
//                  .durationMs(m.getDurationMs())
//                  .updatedAt(m.getUpdatedAt())
//                  .report(summary) // 가공된 summary 객체를 주입
//                  .build();
//            })
//        .toList();
//  }
// }
