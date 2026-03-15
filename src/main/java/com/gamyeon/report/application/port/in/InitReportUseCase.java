package com.gamyeon.report.application.port.in;

public interface InitReportUseCase {

  /** 면접 종료 이벤트 수신 시 REPORTS 레코드 IN_PROGRESS 초기 생성 - 중복 생성 방지 (existsByIntvId 선 확인) */
  void init(Long intvId, Long userId);
}
