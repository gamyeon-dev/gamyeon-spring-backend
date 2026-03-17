package com.gamyeon.report.application.port.out;

import com.gamyeon.question.domain.QuestionSet;
import java.util.List;

public interface LoadQuestionSetPort {
  // 특정 면접에 속한 모든 질문 정보를 가져옴
  List<QuestionSet> findAllByIntvId(Long intvId);
}
