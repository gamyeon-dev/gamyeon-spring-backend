package com.gamyeon.dashboard.application.port.inbound;

import com.gamyeon.dashboard.domain.Notice;
import com.gamyeon.dashboard.domain.NoticeCategory;
import java.time.LocalDateTime;

public record NoticeSummary(
    Long id, NoticeCategory category, String title, LocalDateTime createdAt) {

  public static NoticeSummary from(Notice notice) {
    return new NoticeSummary(
        notice.getId(), notice.getCategory(), notice.getTitle(), notice.getCreatedAt());
  }
}
