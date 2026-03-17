package com.gamyeon.report.application.exception;

import com.gamyeon.common.exception.BaseException;

public class ReportAlreadyExistsException extends BaseException {

  public ReportAlreadyExistsException(Long intvId) {
    super(ReportErrorCode.REPORT_ALREADY_EXISTS);
  }
}
