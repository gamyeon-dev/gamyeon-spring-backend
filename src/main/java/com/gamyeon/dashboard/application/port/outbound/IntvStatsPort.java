package com.gamyeon.dashboard.application.port.outbound;

import com.gamyeon.dashboard.application.port.inbound.DailyStat;

import java.time.LocalDate;
import java.util.List;

public interface IntvStatsPort {
    List<DailyStat> findDailyStats(Long userId, LocalDate startDate, LocalDate endDate);
}
