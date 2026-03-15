package com.gamyeon.report.application.port.in;

import com.gamyeon.report.infrastructure.web.dto.ReportListResponse;
import java.util.List;

public interface GetReportListUseCase {

  /** 사용자 리포트 목록 조회 - INTV + REPORTS LEFT JOIN 결과 반환 - REPORTS 없는 면접도 포함 (report: null) */
  List<ReportListResponse> getList(Long userId);
}
