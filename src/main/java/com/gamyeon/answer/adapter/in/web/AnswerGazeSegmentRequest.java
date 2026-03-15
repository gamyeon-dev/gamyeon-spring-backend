package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record AnswerGazeSegmentRequest(
    @NotNull @Valid Meta meta,
    @NotNull @Valid MetricsSummary metricsSummary,
    @NotNull List<@Valid RawData> rawData,
    @NotNull List<@Valid Event> events) {

  public SendAnswerGazeSegmentCommand toCommand(Long userId, Long questionSetId) {
    return new SendAnswerGazeSegmentCommand(
        userId,
        questionSetId,
        new SendAnswerGazeSegmentCommand.Meta(
            meta.interviewId(), meta.questionId(), meta.timestamp(), meta.segmentSequence()),
        new SendAnswerGazeSegmentCommand.MetricsSummary(
            metricsSummary.averageConcentration(),
            metricsSummary.blinkCount(),
            metricsSummary.isAwayDetected()),
        rawData.stream()
            .map(
                item ->
                    new SendAnswerGazeSegmentCommand.RawData(
                        item.offsetMs(),
                        item.confidence(),
                        new SendAnswerGazeSegmentCommand.Gaze(
                            new SendAnswerGazeSegmentCommand.Eye(
                                item.gaze().left().x(), item.gaze().left().y()),
                            new SendAnswerGazeSegmentCommand.Eye(
                                item.gaze().right().x(), item.gaze().right().y())),
                        new SendAnswerGazeSegmentCommand.Head(
                            item.head().pitch(), item.head().yaw(), item.head().roll())))
            .toList(),
        events.stream()
            .map(
                event ->
                    new SendAnswerGazeSegmentCommand.Event(
                        event.type(), event.offsetMs(), event.direction()))
            .toList());
  }

  public record Meta(
      @NotNull Long interviewId,
      @NotNull Long questionId,
      @NotNull Long timestamp,
      @NotNull Integer segmentSequence) {}

  public record MetricsSummary(
      @NotNull BigDecimal averageConcentration,
      @NotNull Integer blinkCount,
      @NotNull Boolean isAwayDetected) {}

  public record RawData(
      @NotNull Integer offsetMs,
      @NotNull BigDecimal confidence,
      @NotNull @Valid Gaze gaze,
      @NotNull @Valid Head head) {}

  public record Gaze(@NotNull @Valid Eye left, @NotNull @Valid Eye right) {}

  public record Eye(@NotNull BigDecimal x, @NotNull BigDecimal y) {}

  public record Head(
      @NotNull BigDecimal pitch, @NotNull BigDecimal yaw, @NotNull BigDecimal roll) {}

  public record Event(@NotNull String type, @NotNull Integer offsetMs, @NotNull String direction) {}
}
