package com.gamyeon.report.infrastructure.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.gamyeon.feedback.domain.FeedbackStatus;
import com.gamyeon.feedback.domain.event.FeedbackSavedEvent;
import com.gamyeon.feedback.support.TestcontainersSupport; // ✅ 기존 서포트 클래스 임포트
import com.gamyeon.intv.domain.event.InterviewFinishedEvent;
import com.gamyeon.report.application.port.in.GenerateReportUseCase;
import com.gamyeon.report.application.port.in.InitReportUseCase;
import com.gamyeon.report.application.port.in.TriggerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
class ReportEventListenerTest extends TestcontainersSupport { // ✅ 거인의 어깨(Testcontainers)에 올라탑니다!

  @Autowired private ApplicationEventPublisher eventPublisher;

  @Autowired private TransactionTemplate transactionTemplate;

  @MockitoBean private InitReportUseCase initReportUseCase;

  @MockitoBean private GenerateReportUseCase generateReportUseCase;

  // ✅ AWS SDK 초기화 에러를 막기 위한 '가짜' 빈 등록 (지뢰 제거)
  @MockitoBean private software.amazon.awssdk.services.s3.S3Client s3Client;
  @MockitoBean private software.amazon.awssdk.services.s3.presigner.S3Presigner s3Presigner;

  @Test
  @DisplayName("면접 종료 이벤트 발생 시 트랜잭션이 커밋된 후 리포트 초기화와 생성 트리거가 호출된다")
  void shouldHandleInterviewFinishedEventAfterCommit() {
    Long intvId = 1L;
    Long userId = 7L;
    InterviewFinishedEvent event = new InterviewFinishedEvent(intvId, userId);

    transactionTemplate.executeWithoutResult(
        status -> {
          eventPublisher.publishEvent(event);
        });

    verify(initReportUseCase, timeout(2000)).init(intvId, userId);
    verify(generateReportUseCase, timeout(2000))
        .tryTriggerReportGeneration(eq(intvId), eq(TriggerType.INTERVIEW_FINISHED));
  }

  @Test
  @DisplayName("피드백 저장 이벤트가 발생하고 커밋되면 리포트 생성 트리거가 비동기로 호출된다")
  void shouldHandleFeedbackSavedEventAfterCommit() {
    Long intvId = 1L;
    Long questionSetId = 101L;
    // ✅ SUCCESS가 아니라 SUCCEED입니다! (Enum 오타 수정)
    FeedbackSavedEvent event =
        new FeedbackSavedEvent(intvId, questionSetId, FeedbackStatus.SUCCEED);

    transactionTemplate.executeWithoutResult(
        status -> {
          eventPublisher.publishEvent(event);
        });

    verify(generateReportUseCase, timeout(2000))
        .tryTriggerReportGeneration(eq(intvId), eq(TriggerType.FEEDBACK_SAVED));
  }

  @Test
  @DisplayName("트랜잭션이 실패하여 롤백되면 리스너 로직은 실행되지 않아야 한다")
  void shouldNotHandleEventIfTransactionRollsBack() {
    Long intvId = 1L;
    InterviewFinishedEvent event = new InterviewFinishedEvent(intvId, 7L);

    try {
      transactionTemplate.executeWithoutResult(
          status -> {
            eventPublisher.publishEvent(event);
            throw new RuntimeException("테스트용 강제 롤백");
          });
    } catch (RuntimeException ignored) {
    }

    verify(initReportUseCase, after(1000).never()).init(any(), any());
  }
}
