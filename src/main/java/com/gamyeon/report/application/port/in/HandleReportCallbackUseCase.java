package com.gamyeon.report.application.port.in;

import com.gamyeon.report.infrastructure.external.dto.AiReportCallbackRequest;

public interface HandleReportCallbackUseCase {

  /** AI 서버 Callback 수신 처리 - SUCCEED → 컬럼 분리 저장 + report_data jsonb 저장 - FAILED → status: FAILED */
  void handle(AiReportCallbackRequest callback);
}
