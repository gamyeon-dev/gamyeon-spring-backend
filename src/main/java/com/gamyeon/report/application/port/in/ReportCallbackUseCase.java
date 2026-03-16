package com.gamyeon.report.application.port.in;

import com.gamyeon.report.presentation.dto.request.ReportWebhookRequest;

public interface ReportCallbackUseCase {
  /** AI 서버로부터 수신한 최종 리포트 데이터를 처리합니다. 성공 시 status를 SUCCEED로 변경하고 상세 데이터를 저장합니다. */
  void processReportCallback(ReportWebhookRequest request);
}
