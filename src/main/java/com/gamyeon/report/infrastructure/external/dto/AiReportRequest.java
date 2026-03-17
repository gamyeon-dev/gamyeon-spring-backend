package com.gamyeon.report.infrastructure.external.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiReportRequest {

  private Long intvId;
  private Long userId;
  private String callback;
  private List<AiReportFeedbackItem> feedbacks;
}
