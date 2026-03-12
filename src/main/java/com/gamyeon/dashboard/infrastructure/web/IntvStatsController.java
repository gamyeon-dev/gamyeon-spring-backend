package com.gamyeon.dashboard.infrastructure.web;

import com.gamyeon.common.response.SuccessResponse;
import com.gamyeon.dashboard.application.port.inbound.DailyStat;
import com.gamyeon.dashboard.application.port.inbound.IntvStatsUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import com.gamyeon.common.security.CurrentUserId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/intvs")
public class IntvStatsController {

    private final IntvStatsUseCase intvStatsUseCase;

    public IntvStatsController(IntvStatsUseCase intvStatsUseCase) {
        this.intvStatsUseCase = intvStatsUseCase;
    }

    @GetMapping("/stats")
    public ResponseEntity<SuccessResponse<List<DailyStat>>> getIntvStats(
            @CurrentUserId Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<DailyStat> stats = intvStatsUseCase.getIntvStats(userId, startDate, endDate);
        return ResponseEntity.ok(SuccessResponse.of(stats));
    }
}
