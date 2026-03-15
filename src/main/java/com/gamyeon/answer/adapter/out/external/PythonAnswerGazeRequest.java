package com.gamyeon.answer.adapter.out.external;

import com.gamyeon.answer.application.port.out.AnswerGazeSegmentPayload;
import java.util.List;

public record PythonAnswerGazeRequest(
    Long questionId,
    com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentCommand.Meta meta,
    com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentCommand.MetricsSummary
        metricsSummary,
    List<com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentCommand.RawData> rawData,
    List<com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentCommand.Event> events) {

  public static PythonAnswerGazeRequest from(AnswerGazeSegmentPayload payload) {
    return new PythonAnswerGazeRequest(
        payload.questionId(),
        payload.meta(),
        payload.metricsSummary(),
        payload.rawData(),
        payload.events());
  }
}
