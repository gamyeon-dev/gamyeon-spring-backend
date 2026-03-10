package com.gamyeon.dashboard.application.port.inbound;

import com.gamyeon.dashboard.domain.Notice;

import java.time.LocalDateTime;

public record NoticeDetail(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static NoticeDetail from(Notice notice) {
        return new NoticeDetail(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }
}
