package com.gamyeon.report.application.service;

import static org.assertj.core.api.Assertions.assertThat; // AssertJ 사용 권장
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.gamyeon.question.domain.QuestionSet;
import com.gamyeon.report.application.port.in.TriggerType;
import com.gamyeon.report.application.port.out.LoadFeedbackPort;
import com.gamyeon.report.application.port.out.LoadQuestionSetPort;
import com.gamyeon.report.application.port.out.LoadReportPort;
import com.gamyeon.report.application.port.out.SaveReportPort;
import com.gamyeon.report.domain.Report;
import com.gamyeon.report.infrastructure.external.AiReportClient;
import com.gamyeon.report.infrastructure.external.dto.AiReportFeedbackItem;
import com.gamyeon.report.infrastructure.external.dto.AiReportRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReportGenerateServiceTest {

  @InjectMocks private ReportGenerateService reportGenerateService;

  @Mock private LoadReportPort loadReportPort;
  @Mock private SaveReportPort saveReportPort;
  @Mock private LoadFeedbackPort loadFeedbackPort;
  @Mock private LoadQuestionSetPort loadQuestionSetPort;
  @Mock private AiReportClient aiReportClient;

  @BeforeEach
  void setUp() {
    // @Value("${spring.server.callback-base-url}") 필드 주입 시뮬레이션
    ReflectionTestUtils.setField(
        reportGenerateService, "callbackBaseUrl", "http://test-server.com");
  }

  @Test
  @DisplayName("질문 ID가 무작위여도 ID 오름차순으로 정렬되어, 올바른 인덱스와 질문 내용이 AI 요청에 포함된다")
  void shouldAssignCorrectIndicesAndContentBasedOnQuestionIdOrder() {
    // given
    Long intvId = 1L;
    Long userId = 7L;

    // 1. 질문 준비 (생성 순서와 무관하게 ID가 뒤섞여 있다고 가정)
    QuestionSet q1 = QuestionSet.create(intvId, "두 번째 질문입니다."); // ID 102 예정
    QuestionSet q2 = QuestionSet.create(intvId, "첫 번째 질문입니다."); // ID 101 예정
    QuestionSet q3 = QuestionSet.create(intvId, "세 번째 질문입니다."); // ID 103 예정

    ReflectionTestUtils.setField(q1, "id", 102L);
    ReflectionTestUtils.setField(q2, "id", 101L);
    ReflectionTestUtils.setField(q3, "id", 103L);

    // Port는 뒤섞인 리스트를 반환하더라도 서비스에서 정렬해야 함
    given(loadQuestionSetPort.findAllByIntvId(intvId)).willReturn(List.of(q1, q2, q3));

    // 2. 피드백 준비 (인덱스와 내용이 비어있는 상태로 Port에서 넘어옴)
    AiReportFeedbackItem f1 =
        AiReportFeedbackItem.builder().questionSetId(101L).status("SUCCEED").build();
    AiReportFeedbackItem f2 =
        AiReportFeedbackItem.builder().questionSetId(102L).status("SUCCEED").build();
    AiReportFeedbackItem f3 =
        AiReportFeedbackItem.builder().questionSetId(103L).status("SUCCEED").build();

    given(loadFeedbackPort.findSucceedFeedbacksByIntvId(intvId)).willReturn(List.of(f1, f2, f3));
    given(loadFeedbackPort.countSucceedByIntvId(intvId)).willReturn(7);

    // 3. 리포트 및 락 설정
    Report report = Report.createInProgress(intvId, userId);
    given(loadReportPort.findByIntvIdWithLock(intvId)).willReturn(Optional.of(report));

    // when
    reportGenerateService.tryTriggerReportGeneration(intvId, TriggerType.FEEDBACK_SAVED);

    // then
    ArgumentCaptor<AiReportRequest> captor = ArgumentCaptor.forClass(AiReportRequest.class);
    verify(aiReportClient).requestGenerate(captor.capture());

    AiReportRequest actualRequest = captor.getValue();
    List<AiReportFeedbackItem> sentFeedbacks = actualRequest.getFeedbacks();

    // 검증 1: Callback URL 확인
    assertThat(actualRequest.getCallback()).contains("http://test-server.com");

    // 검증 2: 인덱스 및 질문 내용 매칭 확인
    // ID 101 -> Index 1, "첫 번째 질문입니다."
    AiReportFeedbackItem first = findFeedbackById(sentFeedbacks, 101L);
    assertThat(first.getIndex()).isEqualTo(1);
    assertThat(first.getQuestionContent()).isEqualTo("첫 번째 질문입니다.");

    // ID 102 -> Index 2, "두 번째 질문입니다."
    AiReportFeedbackItem second = findFeedbackById(sentFeedbacks, 102L);
    assertThat(second.getIndex()).isEqualTo(2);
    assertThat(second.getQuestionContent()).isEqualTo("두 번째 질문입니다.");

    // ID 103 -> Index 3, "세 번째 질문입니다."
    AiReportFeedbackItem third = findFeedbackById(sentFeedbacks, 103L);
    assertThat(third.getIndex()).isEqualTo(3);
    assertThat(third.getQuestionContent()).isEqualTo("세 번째 질문입니다.");
  }

  private AiReportFeedbackItem findFeedbackById(List<AiReportFeedbackItem> list, Long id) {
    return list.stream()
        .filter(f -> f.getQuestionSetId().equals(id))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Feedback not found for ID: " + id));
  }
}
