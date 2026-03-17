package com.gamyeon.report.application.port.out;

import com.gamyeon.report.domain.Report;

public interface SaveReportPort {

  Report save(Report report);

  boolean existsByIntvId(Long intvId);

  void delete(Report report);
}
