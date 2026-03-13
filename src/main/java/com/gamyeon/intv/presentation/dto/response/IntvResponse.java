package com.gamyeon.intv.presentation.dto.response;

import com.gamyeon.intv.application.dto.result.IntvInfo;

public record IntvResponse(Long intvId, String title, String status) {
  public static IntvResponse from(IntvInfo info) {
    return new IntvResponse(info.intvId(), info.title(), info.status().name());
  }
}
