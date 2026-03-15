package com.gamyeon.report.application.exception;

public class ReportGenerationFailedException extends RuntimeException {
  public ReportGenerationFailedException(Long reportId) {
    super("report 생성실패:  id=" + reportId);
  }
}
