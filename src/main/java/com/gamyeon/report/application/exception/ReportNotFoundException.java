package com.gamyeon.report.application.exception;

import com.gamyeon.common.exception.BaseException;

public class ReportNotFoundException extends BaseException {

  public ReportNotFoundException(Long intvId) {
    super(ReportErrorCode.REPORT_NOT_FOUND);
  }
}
