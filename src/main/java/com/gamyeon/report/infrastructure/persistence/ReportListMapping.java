package com.gamyeon.report.infrastructure.persistence;

import java.time.LocalDateTime;

public interface ReportListMapping {
  Long getIntvId();

  String getIntvTitle(); // 추가

  String getIntvStatus();

  Long getDurationMs(); // 추가

  LocalDateTime getUpdatedAt(); // DTO 명칭에 맞춤

  // 리포트 관련 필드
  Long getReportId();

  String getReportStatus();

  Integer getTotalScore();

  Integer getAnsweredCount();
}
