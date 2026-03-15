package com.gamyeon.answer.application.port.out;

import com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentCommand;
import java.util.List;

public record AnswerGazeSegmentPayload(
    Long questionId,
    SendAnswerGazeSegmentCommand.Meta meta,
    SendAnswerGazeSegmentCommand.MetricsSummary metricsSummary,
    List<SendAnswerGazeSegmentCommand.RawData> rawData,
    List<SendAnswerGazeSegmentCommand.Event> events) {}
