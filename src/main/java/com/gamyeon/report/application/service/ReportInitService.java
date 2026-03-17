package com.gamyeon.report.application.service;

import com.gamyeon.report.application.port.in.InitReportUseCase;
import com.gamyeon.report.application.port.out.SaveReportPort;
import com.gamyeon.report.domain.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportInitService implements InitReportUseCase {

  private final SaveReportPort saveReportPort;

  @Override
  @Transactional
  public void init(Long intvId, Long userId) {
    if (saveReportPort.existsByIntvId(intvId)) {
      log.warn("[Report] 중복 생성 방지 - 이미 존재하는 REPORTS - intvId={}", intvId);
      return;
    }
    Report report = Report.createInProgress(intvId, userId);
    saveReportPort.save(report);
    log.info("[Report] REPORTS IN_PROGRESS 생성 완료 - intvId={}", intvId);
  }
}
