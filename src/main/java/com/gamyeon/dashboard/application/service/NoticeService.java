package com.gamyeon.dashboard.application.service;

import com.gamyeon.common.exception.CommonErrorCode;
import com.gamyeon.common.exception.CommonException;
import com.gamyeon.dashboard.application.port.inbound.NoticeDetail;
import com.gamyeon.dashboard.application.port.inbound.NoticeSummary;
import com.gamyeon.dashboard.application.port.inbound.NoticeUseCase;
import com.gamyeon.dashboard.application.port.outbound.NoticeRepository;
import com.gamyeon.dashboard.domain.Notice;
import java.util.List;

public class NoticeService implements NoticeUseCase {

  private final NoticeRepository noticeRepository;

  public NoticeService(NoticeRepository noticeRepository) {
    this.noticeRepository = noticeRepository;
  }

  @Override
  public List<NoticeSummary> getNotices() {
    return noticeRepository.findAll().stream().map(NoticeSummary::from).toList();
  }

  @Override
  public NoticeDetail getNotice(Long noticeId) {
    Notice notice =
        noticeRepository
            .findById(noticeId)
            .orElseThrow(() -> new CommonException(CommonErrorCode.NOT_FOUND));
    return NoticeDetail.from(notice);
  }
}
