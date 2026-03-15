package com.gamyeon.report.application.port.out;

import com.gamyeon.report.infrastructure.external.dto.AiReportFeedbackItem;
import java.util.List;

public interface LoadFeedbackPort {

  // 해당 intvId의 SUCCEED 피드백 수 조회
  int countSucceedByIntvId(Long intvId);

  // AI 요청용 피드백 데이터 수합 (SUCCEED만)
  List<AiReportFeedbackItem> findSucceedFeedbacksByIntvId(Long intvId);
}
