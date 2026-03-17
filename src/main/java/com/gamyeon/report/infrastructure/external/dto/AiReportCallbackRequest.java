package com.gamyeon.report.infrastructure.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiReportCallbackRequest {

  @JsonProperty("intvId")
  private Long intvId;

  @JsonProperty("userId")
  private Long userId;

  @JsonProperty("status")
  private String status; // "SUCCEED" | "FAILED"

  @JsonProperty("report")
  private ReportData report; // FAILED 시 null

  @JsonProperty("errorMessage")
  private String errorMessage; // FAILED 시 메시지

  // ── 중첩 DTO ───────────────────────────────────────────────────────────────

  @Getter
  @NoArgsConstructor
  public static class ReportData {

    @JsonProperty("totalScore")
    private Integer totalScore;

    @JsonProperty("reportAccuracy")
    private String reportAccuracy;

    @JsonProperty("jobCategory")
    private String jobCategory;

    @JsonProperty("answeredCount")
    private Integer answeredCount;

    @JsonProperty("avgAnswerDurationMs")
    private Long avgAnswerDurationMs;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("competencyScores")
    private Map<String, Integer> competencyScores;

    @JsonProperty("strengths")
    private List<String> strengths;

    @JsonProperty("weaknesses")
    private List<String> weaknesses;

    @JsonProperty("questionSummaries")
    private List<Map<String, Object>> questionSummaries;
  }
}
