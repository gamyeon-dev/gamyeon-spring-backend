package com.gamyeon.report.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.gamyeon.report.application.exception.ReportGenerationFailedException;
import com.gamyeon.report.application.port.in.TriggerType;
import com.gamyeon.report.application.port.out.LoadFeedbackPort;
import com.gamyeon.report.application.port.out.LoadReportPort;
import com.gamyeon.report.application.port.out.SaveReportPort;
import com.gamyeon.report.domain.Report;
import com.gamyeon.report.domain.ReportStatus;
import com.gamyeon.report.infrastructure.external.AiReportClient;
import com.gamyeon.report.infrastructure.external.dto.AiReportRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class) // Mockito 사용을 선언합니다.
class ReportGenerateServiceTest {

  @Mock private LoadReportPort loadReportPort;
  @Mock private SaveReportPort saveReportPort;
  @Mock private LoadFeedbackPort loadFeedbackPort;
  @Mock private AiReportClient aiReportClient;

  @InjectMocks private ReportGenerateService reportGenerateService;

  private final String MOCK_CALLBACK_URL = "http://test-server.com";

  @BeforeEach
  void setUp() {
    // @Value("${spring.server.callback-base-url}") 필드에 수동으로 값을 주입합니다.
    ReflectionTestUtils.setField(reportGenerateService, "callbackBaseUrl", MOCK_CALLBACK_URL);
  }

  @Test
  @DisplayName("이벤트 트리거: 피드백 수집이 완료(7개)되면 AI 서버에 생성을 요청한다")
  void handleEventTrigger_Success() {
    // given
    Long intvId = 1L;
    Report report = Report.createInProgress(intvId, 100L); // 도메인 객체 생성

    given(loadReportPort.findByIntvIdWithLock(intvId)).willReturn(Optional.of(report));
    given(loadFeedbackPort.countSucceedByIntvId(intvId)).willReturn(7); // 7개 완료 가정
    given(loadFeedbackPort.findSucceedFeedbacksByIntvId(intvId)).willReturn(List.of());

    // when
    reportGenerateService.tryTriggerReportGeneration(intvId, TriggerType.FEEDBACK_SAVED);

    // then
    // AI 클라이언트가 1번 호출되었는지 검증
    verify(aiReportClient, times(1)).requestGenerate(any(AiReportRequest.class));
  }

  @Test
  @DisplayName("스케줄러 트리거: 피드백이 부족(3개 미만)하면 리포트를 실패 처리한다")
  void handleSchedulerTrigger_Fail_WhenLackOfFeedback() {
    // given
    Long intvId = 1L;
    Report report = Report.createInProgress(intvId, 100L);

    given(loadReportPort.findByIntvIdWithLock(intvId)).willReturn(Optional.of(report));
    given(loadFeedbackPort.countSucceedByIntvId(intvId)).willReturn(2); // 2개만 완료 가정

    // when
    reportGenerateService.tryTriggerReportGeneration(intvId, TriggerType.SCHEDULER);

    // then
    // AI 호출은 없어야 하고, 리포트 상태는 실패로 저장되어야 함
    verify(aiReportClient, never()).requestGenerate(any());
    verify(saveReportPort, times(1)).save(argThat(r -> r.getStatus() == ReportStatus.FAILED));
  }

  @Test
  @DisplayName("AI 서버 호출 실패 시 예외를 던지고 리포트를 실패 상태로 저장한다")
  void requestToAi_ThrowsException_OnClientError() {
    // given
    Long intvId = 1L;
    Report report = Report.createInProgress(intvId, 100L);

    given(loadReportPort.findByIntvIdWithLock(intvId)).willReturn(Optional.of(report));
    given(loadFeedbackPort.countSucceedByIntvId(intvId)).willReturn(7);
    // AI 클라이언트 호출 시 강제로 예외 발생
    doThrow(new RuntimeException("AI 서버 장애")).when(aiReportClient).requestGenerate(any());

    // when & then
    assertThrows(
        ReportGenerationFailedException.class,
        () -> reportGenerateService.tryTriggerReportGeneration(intvId, TriggerType.FEEDBACK_SAVED));
    verify(saveReportPort).save(argThat(r -> r.getStatus() == ReportStatus.FAILED));
  }
}
