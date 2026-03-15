package com.gamyeon.report.application.service;

import com.gamyeon.report.application.exception.ReportGenerationFailedException;
import com.gamyeon.report.application.port.in.HandleReportCallbackUseCase;
import com.gamyeon.report.application.port.out.LoadReportPort;
import com.gamyeon.report.application.port.out.SaveReportPort;
import com.gamyeon.report.domain.Report;
import com.gamyeon.report.infrastructure.external.dto.AiReportCallbackRequest;
import com.gamyeon.report.infrastructure.external.dto.AiReportCallbackRequest.ReportData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportCallbackService implements HandleReportCallbackUseCase {

  private final LoadReportPort loadReportPort;
  private final SaveReportPort saveReportPort;

  @Override
  @Transactional
  public void handle(AiReportCallbackRequest callback) {
    Report report =
        loadReportPort
            .findByIntvId(callback.getIntvId())
            .orElseThrow(
                () -> {
                  log.error(
                      "[Report] Callback 수신 — REPORTS 레코드 없음 - intvId={}", callback.getIntvId());
                  return new ReportGenerationFailedException(callback.getIntvId());
                });

    if ("FAILED".equals(callback.getStatus())) {
      log.warn("[Report] AI FAILED Callback 수신 - intvId={}", callback.getIntvId());
      report.fail();
      saveReportPort.save(report);
      return;
    }

    ReportData data = callback.getReport();

    report.complete(
        data.getTotalScore(),
        data.getAnsweredCount(),
        data.getStrengths(),
        data.getWeaknesses(),
        data // report_data jsonb 원본 저장
        );
    saveReportPort.save(report);
    log.info("[Report] 리포트 저장 완료 - intvId={}", callback.getIntvId());
  }
}
