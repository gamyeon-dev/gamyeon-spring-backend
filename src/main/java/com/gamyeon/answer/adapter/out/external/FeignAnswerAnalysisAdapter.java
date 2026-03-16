package com.gamyeon.answer.adapter.out.external;

import com.gamyeon.answer.application.port.out.AnswerAnalysisTarget;
import com.gamyeon.answer.application.port.out.RequestAnswerSttAnalysisPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignAnswerAnalysisAdapter implements RequestAnswerSttAnalysisPort {

  private final PythonAnswerAnalysisFeignClient pythonAnswerAnalysisFeignClient;

  @Override
  public void request(AnswerAnalysisTarget target) {
    pythonAnswerAnalysisFeignClient.requestAnalysis(PythonAnswerAnalysisRequest.from(target));
  }
}
