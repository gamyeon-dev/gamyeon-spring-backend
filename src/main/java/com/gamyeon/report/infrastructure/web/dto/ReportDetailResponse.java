package com.gamyeon.report.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDetailResponse {

  private Long interviewId;
  private String interviewStatus;
  private String reportStatus;
  private ReportDetail report;

  @Getter
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ReportDetail {

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
    private List<QuestionSummary> questionSummaries;
  }

  @Getter
  @Builder
  public static class QuestionSummary {
    private Integer index;
    private String question;
    private String answerSummary;
    private List<String> feedbackBadges;
    private QuestionFeedback feedback;
  }

  @Getter
  @Builder
  public static class QuestionFeedback {
    private String characteristic;
    private String strength;
    private String improvement;
  }
}
