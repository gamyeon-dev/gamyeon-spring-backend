package com.gamyeon.answer.application.port.out;

public interface SendAnswerGazeToAiPort {

  void send(AnswerGazeSegmentPayload payload);
}
