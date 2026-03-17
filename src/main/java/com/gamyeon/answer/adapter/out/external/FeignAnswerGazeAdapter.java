package com.gamyeon.answer.adapter.out.external;

import com.gamyeon.answer.application.port.out.AnswerGazeSegmentPayload;
import com.gamyeon.answer.application.port.out.SendAnswerGazeToAiPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeignAnswerGazeAdapter implements SendAnswerGazeToAiPort {

  private final PythonAnswerGazeFeignClient pythonAnswerGazeFeignClient;

  @Override
  public void send(AnswerGazeSegmentPayload payload) {
    log.info("Calling Python gaze API. questionSetId={}", payload.questionSetId());
    pythonAnswerGazeFeignClient.send(payload.body());
  }
}
