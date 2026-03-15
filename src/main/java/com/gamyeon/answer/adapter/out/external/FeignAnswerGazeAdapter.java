package com.gamyeon.answer.adapter.out.external;

import com.gamyeon.answer.application.port.out.AnswerGazeSegmentPayload;
import com.gamyeon.answer.application.port.out.SendAnswerGazeToAiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignAnswerGazeAdapter implements SendAnswerGazeToAiPort {

  private final PythonAnswerGazeFeignClient pythonAnswerGazeFeignClient;

  @Override
  public void send(AnswerGazeSegmentPayload payload) {
    pythonAnswerGazeFeignClient.send(PythonAnswerGazeRequest.from(payload));
  }
}
