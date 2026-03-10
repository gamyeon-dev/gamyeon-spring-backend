package com.gamyeon.dashboard.application.port.inbound;

import com.gamyeon.dashboard.domain.Notice;

import java.time.LocalDateTime;

public record NoticeSummary(Long id, String title, LocalDateTime createdAt) {

    public static NoticeSummary from(Notice notice) {
        return new NoticeSummary(notice.getId(), notice.getTitle(), notice.getCreatedAt());
    }
}
