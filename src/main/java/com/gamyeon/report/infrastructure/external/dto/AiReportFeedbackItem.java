package com.gamyeon.report.infrastructure.external.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiReportFeedbackItem {

  private Long questionSetId;
  private String status; // "SUCCEED" | "FAILED"

  private Integer reliability; // reliability-> answerAccuracy
  private Integer logicScore;
  private Integer answerCompositionScore;
  private Integer gazeScore;
  private Integer timeScore;
  private Integer answerDurationMs;
  private Integer keywordCount;

  private String characteristic;
  private String answerSummary;
  private String strength;
  private String improvement;
  private List<String> feedbackBadges;
}
