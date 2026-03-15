package com.gamyeon.answer.application.port.in;

import java.math.BigDecimal;
import java.util.List;

public record SendAnswerGazeSegmentCommand(
    Long userId,
    Long questionSetId,
    Meta meta,
    MetricsSummary metricsSummary,
    List<RawData> rawData,
    List<Event> events) {

  public record Meta(Long interviewId, Long questionId, Long timestamp, Integer segmentSequence) {}

  public record MetricsSummary(
      BigDecimal averageConcentration, Integer blinkCount, Boolean isAwayDetected) {}

  public record RawData(Integer offsetMs, BigDecimal confidence, Gaze gaze, Head head) {}

  public record Gaze(Eye left, Eye right) {}

  public record Eye(BigDecimal x, BigDecimal y) {}

  public record Head(BigDecimal pitch, BigDecimal yaw, BigDecimal roll) {}

  public record Event(String type, Integer offsetMs, String direction) {}
}
