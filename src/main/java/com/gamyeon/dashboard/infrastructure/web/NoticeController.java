package com.gamyeon.dashboard.infrastructure.web;

import com.gamyeon.common.response.SuccessResponse;
import com.gamyeon.dashboard.application.port.inbound.NoticeDetail;
import com.gamyeon.dashboard.application.port.inbound.NoticeSummary;
import com.gamyeon.dashboard.application.port.inbound.NoticeUseCase;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notices")
@Slf4j
public class NoticeController {

  private final NoticeUseCase noticeUseCase;

  public NoticeController(NoticeUseCase noticeUseCase) {
    this.noticeUseCase = noticeUseCase;
  }

  @GetMapping
  public ResponseEntity<SuccessResponse<List<NoticeSummary>>> getNotices() {
    log.info("Received get notices request.");
    List<NoticeSummary> notices = noticeUseCase.getNotices();
    return ResponseEntity.ok(SuccessResponse.of(notices));
  }

  @GetMapping("/{noticeId}")
  public ResponseEntity<SuccessResponse<NoticeDetail>> getNotice(@PathVariable Long noticeId) {
    log.info("Received get notice detail request. noticeId={}", noticeId);
    NoticeDetail notice = noticeUseCase.getNotice(noticeId);
    return ResponseEntity.ok(SuccessResponse.of(notice));
  }
}
