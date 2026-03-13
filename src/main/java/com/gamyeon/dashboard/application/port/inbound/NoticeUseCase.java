package com.gamyeon.dashboard.application.port.inbound;

import java.util.List;

public interface NoticeUseCase {
  List<NoticeSummary> getNotices();

  NoticeDetail getNotice(Long noticeId);
}
