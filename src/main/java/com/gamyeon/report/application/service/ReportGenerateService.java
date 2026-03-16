package com.gamyeon.report.application.service;

import com.gamyeon.question.domain.QuestionSet;
import com.gamyeon.report.application.exception.ReportGenerationFailedException;
import com.gamyeon.report.application.port.in.GenerateReportUseCase;
import com.gamyeon.report.application.port.in.TriggerType;
import com.gamyeon.report.application.port.out.LoadFeedbackPort;
import com.gamyeon.report.application.port.out.LoadQuestionSetPort;
import com.gamyeon.report.application.port.out.LoadReportPort;
import com.gamyeon.report.application.port.out.SaveReportPort;
import com.gamyeon.report.domain.Report;
import com.gamyeon.report.domain.ReportStatus;
import com.gamyeon.report.infrastructure.external.AiReportClient;
import com.gamyeon.report.infrastructure.external.dto.AiReportFeedbackItem;
import com.gamyeon.report.infrastructure.external.dto.AiReportRequest;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportGenerateService implements GenerateReportUseCase {

  private static final int TOTAL_QUESTION_COUNT = 7;
  private static final int MIN_FEEDBACK_COUNT = 3;

  private final LoadReportPort loadReportPort;
  private final SaveReportPort saveReportPort;
  private final LoadFeedbackPort loadFeedbackPort;
  private final AiReportClient aiReportClient;
  private final LoadQuestionSetPort loadQuestionSetPort;

  @Value("${spring.server.callback-base-url}")
  private String callbackBaseUrl;

  // ── 단일 진입점 ────────────────────────────────────────────────────────────

  @Override
  @Transactional
  public void tryTriggerReportGeneration(Long intvId, TriggerType triggerType) {

    // Pessimistic Write Lock 획득
    Report report =
        loadReportPort
            .findByIntvIdWithLock(intvId)
            .orElseGet(
                () -> {
                  log.warn("[Report] REPORTS 레코드 없음 - intvId={}", intvId);
                  return null;
                });

    if (report == null) return;

    // IN_PROGRESS가 아니면 이미 처리된 건 — 즉시 리턴
    if (report.getStatus() != ReportStatus.IN_PROGRESS) {
      log.info("[Report] 이미 처리된 리포트 - intvId={}, status={}", intvId, report.getStatus());
      return;
    }

    int succeedCount = loadFeedbackPort.countSucceedByIntvId(intvId);

    switch (triggerType) {
      case INTERVIEW_FINISHED, FEEDBACK_SAVED -> handleEventTrigger(report, succeedCount);
      case SCHEDULER -> handleSchedulerTrigger(report, succeedCount);
    }
  }

  // ── 이벤트 트리거 분기 ─────────────────────────────────────────────────────

  private void handleEventTrigger(Report report, int succeedCount) {
    if (succeedCount >= TOTAL_QUESTION_COUNT) {
      log.info("[Report] 피드백 {}개 완료 — AI 요청 진행 - intvId={}", succeedCount, report.getIntvId());
      requestToAi(report);
    } else {
      log.info("[Report] 피드백 {}개 수집 중 — 대기 - intvId={}", succeedCount, report.getIntvId());
    }
  }

  // ── 스케줄러 트리거 분기 ───────────────────────────────────────────────────

  private void handleSchedulerTrigger(Report report, int succeedCount) {
    if (succeedCount >= MIN_FEEDBACK_COUNT) {
      log.info("[Report] 스케줄러 강제 실행 - intvId={}, 피드백 수={}", report.getIntvId(), succeedCount);
      requestToAi(report);
    } else {
      log.warn("[Report] 피드백 부족 FAILED 처리 - intvId={}, 피드백 수={}", report.getIntvId(), succeedCount);
      report.fail();
      saveReportPort.save(report);
    }
  }

  // ── AI 서버 호출 ──────────────────────────────────────────────────────────

  private void requestToAi(Report report) {
    Long intvId = report.getIntvId();

    // 1. 질문 리스트를 ID 순으로 정렬하여 로드
    List<QuestionSet> questions =
        loadQuestionSetPort.findAllByIntvId(intvId).stream()
            .sorted(Comparator.comparing(QuestionSet::getId))
            .toList();

    // 2. 피드백 수집
    List<AiReportFeedbackItem> feedbacks = loadFeedbackPort.findSucceedFeedbacksByIntvId(intvId);

    // 3. 인덱스와 질문 내용을 매핑하여 AI 전송용 리스트 생성
    List<AiReportFeedbackItem> enrichedFeedbacks =
        feedbacks.stream()
            .map(
                f -> {
                  QuestionSet q =
                      questions.stream()
                          .filter(qs -> qs.getId().equals(f.getQuestionSetId()))
                          .findFirst()
                          .orElseThrow();

                  int index = questions.indexOf(q) + 1;

                  // DTO에 정의된 toBuilder()를 사용하여 index와 content를 주입
                  return f.toBuilder().index(index).questionContent(q.getContent()).build();
                })
            .toList();

    // 4. [핵심] 가공된 enrichedFeedbacks를 담아 최종 요청 객체 생성
    AiReportRequest request =
        AiReportRequest.builder()
            .intvId(intvId)
            .userId(report.getUserId())
            .callback(callbackBaseUrl + "/internal/v1/reports/callback")
            .feedbacks(enrichedFeedbacks) // 여기서 가공된 리스트 주입!
            .build();

    try {
      aiReportClient.requestGenerate(request);
      log.info("[Report] AI 리포트 생성 요청 완료 - intvId={}", intvId);
    } catch (Exception e) {
      log.error("[Report] AI 리포트 생성 요청 실패 - intvId={}", intvId, e);
      report.fail();
      saveReportPort.save(report);
      throw new ReportGenerationFailedException(intvId); // RPRT-E001
    }
  }
}
