package com.gamyeon.report.application.port.in;

public interface GenerateReportUseCase {

  /**
   * 리포트 생성 트리거 단일 진입점 - InterviewFinishedEvent 수신 시 (REPORTS 레코드 생성 + 즉시/대기 분기) -
   * FeedbackSavedEvent 수신 시 (7개 도달 여부 확인) - 스케줄러 호출 시 (5분 타임아웃 강제 실행/실패)
   */
  void tryTriggerReportGeneration(Long intvId, TriggerType triggerType);
}
