package com.gamyeon.report.application.port.in;

public interface DeleteReportUseCase {

  /** 리포트 삭제 - 본인 소유 검증 후 삭제 - 리포트 없음 → RPRT-N001 */
  void delete(Long intvId, Long userId);
}
