package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.preparation.application.port.in.PreparationFileCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PreparationFilesRegisterRequest(
    @NotEmpty(message = "등록할 파일은 최소 1개 이상이어야 합니다.")
        List<@Valid PreparationFileRegisterItemRequest> files) {

  public List<PreparationFileCommand> toCommands(Long userId, Long intvId) {
    return files.stream().map(file -> file.toCommand(userId, intvId)).toList();
  }
}
