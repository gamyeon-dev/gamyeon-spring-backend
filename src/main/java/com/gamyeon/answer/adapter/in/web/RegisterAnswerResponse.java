package com.gamyeon.answer.adapter.in.web;

import com.gamyeon.answer.application.port.in.RegisterAnswerResult;

public record RegisterAnswerResponse(Long answerId, Long questionSetId) {

  public static RegisterAnswerResponse from(RegisterAnswerResult result) {
    return new RegisterAnswerResponse(result.answerId(), result.questionSetId());
  }
}
