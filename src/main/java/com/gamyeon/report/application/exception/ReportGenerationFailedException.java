package com.gamyeon.report.application.exception;

import com.gamyeon.common.exception.BaseException;

public class ReportGenerationFailedException extends BaseException {

  public ReportGenerationFailedException(Long intvId) {
    super(ReportErrorCode.REPORT_GENERATION_FAILED);
  }
}
