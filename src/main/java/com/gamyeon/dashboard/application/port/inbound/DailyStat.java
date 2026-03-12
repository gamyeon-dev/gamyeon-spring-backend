package com.gamyeon.dashboard.application.port.inbound;

import java.time.LocalDate;

public record DailyStat(LocalDate date, long count) {}
