package com.gamyeon.dashboard.application.port.inbound;

import java.time.LocalDate;
import java.util.List;

public interface IntvStatsUseCase {
    List<DailyStat> getIntvStats(Long userId, LocalDate startDate, LocalDate endDate);
}
