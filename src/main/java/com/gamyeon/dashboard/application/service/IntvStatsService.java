package com.gamyeon.dashboard.application.service;

import com.gamyeon.dashboard.application.port.inbound.DailyStat;
import com.gamyeon.dashboard.application.port.inbound.IntvStatsUseCase;
import com.gamyeon.dashboard.application.port.outbound.IntvStatsPort;
import java.time.LocalDate;
import java.util.List;

public class IntvStatsService implements IntvStatsUseCase {

  private final IntvStatsPort intvStatsPort;

  public IntvStatsService(IntvStatsPort intvStatsPort) {
    this.intvStatsPort = intvStatsPort;
  }

  @Override
  public List<DailyStat> getIntvStats(Long userId, LocalDate startDate, LocalDate endDate) {
    return intvStatsPort.findDailyStats(userId, startDate, endDate);
  }
}
