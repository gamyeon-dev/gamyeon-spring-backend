package com.gamyeon.report.infrastructure.external;

import com.gamyeon.report.application.port.out.LoadFeedbackPort;
import com.gamyeon.report.infrastructure.external.dto.AiReportFeedbackItem;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedbackLoadAdapter implements LoadFeedbackPort {

  // TODO: 추후 feedback 패키지의 Repository나 QueryService를 주입받아 실제 데이터 조회 연동 필요

  @Override
  public int countSucceedByIntvId(Long intvId) {
    // [임시 하드코딩] 테스트를 위해 무조건 7 반환하거나 DB 연동 전까지 0 반환
    return 0;
  }

  @Override
  public List<AiReportFeedbackItem> findSucceedFeedbacksByIntvId(Long intvId) {
    // [임시 하드코딩]
    return Collections.emptyList();
  }
}
