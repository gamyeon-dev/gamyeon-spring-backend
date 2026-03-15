package com.gamyeon.report.infrastructure.event;

import com.gamyeon.feedback.domain.event.FeedbackSavedEvent;
import com.gamyeon.intv.domain.event.InterviewFinishedEvent;
import com.gamyeon.report.application.port.in.GenerateReportUseCase;
import com.gamyeon.report.application.port.in.InitReportUseCase;
import com.gamyeon.report.application.port.in.TriggerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportEventListener {

  private final InitReportUseCase initReportUseCase;
  private final GenerateReportUseCase generateReportUseCase;

  // ── 면접 종료 이벤트 수신 ──────────────────────────────────────────────────

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onInterviewFinished(InterviewFinishedEvent event) {
    log.info("[Report] 면접 종료 이벤트 수신 - intvId={}", event.intvId());

    // 1. REPORTS 레코드 IN_PROGRESS 생성
    initReportUseCase.init(event.intvId(), event.userId());

    // 2. 즉시 트리거 시도 (면접 종료 시점에 이미 피드백 7개인 경우 대응)
    generateReportUseCase.tryTriggerReportGeneration(
        event.intvId(), TriggerType.INTERVIEW_FINISHED);
  }

  // ── 피드백 저장 완료 이벤트 수신 ──────────────────────────────────────────

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onFeedbackSaved(FeedbackSavedEvent event) {
    log.info(
        "[Report] 피드백 저장 이벤트 수신 - intvId={}, questionSetId={}",
        event.intvId(),
        event.questionSetId());

    generateReportUseCase.tryTriggerReportGeneration(event.intvId(), TriggerType.FEEDBACK_SAVED);
  }
}
