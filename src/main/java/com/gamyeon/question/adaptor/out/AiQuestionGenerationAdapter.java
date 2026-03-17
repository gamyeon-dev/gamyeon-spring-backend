package com.gamyeon.question.adaptor.out;

import com.gamyeon.question.application.port.out.GenerateCustomQuestionPort;
import com.gamyeon.question.application.port.out.PreparationForQuestionGeneration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiQuestionGenerationAdapter implements GenerateCustomQuestionPort {

  private final PythonQuestionGenerationFeignClient pythonQuestionGenerationFeignClient;

  @Override
  public void request(PreparationForQuestionGeneration preparation, String callbackUrl) {
    AiQuestionGenerationRequest request =
        AiQuestionGenerationRequest.from(preparation, callbackUrl);
    pythonQuestionGenerationFeignClient.requestGeneration(request);
  }
}
