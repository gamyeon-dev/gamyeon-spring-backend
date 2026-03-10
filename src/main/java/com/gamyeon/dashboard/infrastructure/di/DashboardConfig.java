package com.gamyeon.dashboard.infrastructure.di;

import com.gamyeon.dashboard.application.port.inbound.IntvStatsUseCase;
import com.gamyeon.dashboard.application.port.inbound.NoticeUseCase;
import com.gamyeon.dashboard.application.port.outbound.IntvStatsPort;
import com.gamyeon.dashboard.application.port.outbound.NoticeRepository;
import com.gamyeon.dashboard.application.service.IntvStatsService;
import com.gamyeon.dashboard.application.service.NoticeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashboardConfig {

    @Bean
    public IntvStatsUseCase intvStatsUseCase(IntvStatsPort intvStatsPort) {
        return new IntvStatsService(intvStatsPort);
    }

    @Bean
    public NoticeUseCase noticeUseCase(NoticeRepository noticeRepository) {
        return new NoticeService(noticeRepository);
    }
}
