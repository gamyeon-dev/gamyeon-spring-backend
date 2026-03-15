package com.gamyeon.report.application.port.in;

public enum TriggerType {
  INTERVIEW_FINISHED, // intv 종료 이벤트 → REPORTS 레코드 생성 + 즉시 분기
  FEEDBACK_SAVED, // 피드백 저장 이벤트 → 7개 도달 여부 확인
  SCHEDULER // 스케줄러 → 5분 타임아웃 강제 실행/실패
}
