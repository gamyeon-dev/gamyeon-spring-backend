package com.gamyeon.question.adaptor.out;

import com.gamyeon.question.application.port.out.GenerateCustomQuestionPort;
import com.gamyeon.question.application.port.out.PreparationForQuestionGeneration;
import org.springframework.stereotype.Repository;

@Repository
public class AiQuestionGenerationAdapter implements GenerateCustomQuestionPort {

  @Override
  public void request(PreparationForQuestionGeneration preparation, String callbackUrl) {
    AiQuestionGenerationRequest request =
        AiQuestionGenerationRequest.from(preparation, callbackUrl);

    // TODO : 여기서 AI API 호출!!
    // @PythonApi(request)(/internal/v1/questions/generate)
  }
}
