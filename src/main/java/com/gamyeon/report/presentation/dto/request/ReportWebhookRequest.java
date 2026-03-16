package com.gamyeon.report.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportWebhookRequest {
  private Long intvId;
  private Long userId;
  private String status; // SUCCEED | FAILED
  private ReportDetailData report;
  private String errorMessage;

  @Getter
  @NoArgsConstructor
  public static class ReportDetailData {
    @JsonProperty("total_score")
    private Integer totalScore;

    @JsonProperty("report_accuracy")
    private String reportAccuracy;

    @JsonProperty("job_category")
    private String jobCategory;

    @JsonProperty("answered_count")
    private Integer answeredCount;

    @JsonProperty("avg_answer_duration_ms")
    private Long avgAnswerDurationMs;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("competency_scores")
    private Map<String, Integer> competencyScores;

    private List<String> strengths;
    private List<String> weaknesses;

    @JsonProperty("question_summaries")
    private List<Object> questionSummaries; // 상세 내용은 jsonb로 통째 저장하므로 Object 처리
  }
}
