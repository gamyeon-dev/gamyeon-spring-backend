package com.gamyeon.report.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gamyeon.report.domain.Report;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportListResponse {

    private Long intvId;
    private String intvTitle;
    private String intvStatus;
    private Long durationMs;
    private LocalDateTime updatedAt;
    private ReportSummary report;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReportSummary {
        private Long reportId;
        private String reportStatus;
        private Integer totalScore;
        private Integer answeredCount;
        private List<String> strengths;
        private List<String> weaknesses;

        // 중복 방지를 위해 내부 클래스 안에 static 메서드를 둡니다.
        public static ReportSummary fromReport(Report report) {
            if (report == null) return null;
            return ReportSummary.builder()
                    .reportId(report.getId())
                    .reportStatus(report.getStatus().name())
                    .totalScore(report.getTotalScore())
                    .answeredCount(report.getAnsweredCount())
                    .strengths(report.getStrengths())
                    .weaknesses(report.getWeaknesses())
                    .build();
        }
    }

    // ── 도메인 → DTO 변환 ──────────────────────────────────────────────────────

    // 일단 컴파일이 통과되도록 개별 필드를 받는 형태로 임시 작성했습니다.
    // 추후 Intv 엔티티를 파라미터로 받도록 변경하시면 됩니다.
    public static ReportListResponse of(
            Long intvId,
            String intvTitle,
            String intvStatus,
            Long durationMs,
            LocalDateTime updatedAt,
            Report report) {

        return ReportListResponse.builder()
                .intvId(intvId)
                .intvTitle(intvTitle)
                .intvStatus(intvStatus)
                .durationMs(durationMs)
                .updatedAt(updatedAt)
                .report(ReportSummary.fromReport(report))
                .build();
    }
}
