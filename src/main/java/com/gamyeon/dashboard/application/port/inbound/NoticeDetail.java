package com.gamyeon.dashboard.application.port.inbound;

import com.gamyeon.dashboard.domain.Notice;

import java.time.LocalDateTime;
import java.util.List;

public record NoticeDetail(Long id, String title, String content, List<String> imageUrls, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static NoticeDetail from(Notice notice) {
        List<String> imageUrls = notice.getImages().stream()
                .map(image -> image.getImageUrl())
                .toList();
        return new NoticeDetail(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                imageUrls,
                notice.getCreatedAt(),
                notice.getUpdatedAt()
        );
    }
}
