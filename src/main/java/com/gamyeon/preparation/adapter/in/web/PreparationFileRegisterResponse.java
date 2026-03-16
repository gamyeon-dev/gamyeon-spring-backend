package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.preparation.application.port.in.PreparationFilesRegisterResult;
import com.gamyeon.preparation.domain.PreparationStatus;

public record PreparationFileRegisterResponse(
    Long preparationId, PreparationStatus preparationStatus) {

  public static PreparationFileRegisterResponse from(PreparationFilesRegisterResult result) {
    return new PreparationFileRegisterResponse(result.preparationId(), result.preparationStatus());
  }
}
