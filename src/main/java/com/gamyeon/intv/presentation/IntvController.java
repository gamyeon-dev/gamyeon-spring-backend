package com.gamyeon.intv.presentation;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.intv.application.dto.command.ChangeStateIntvCommand;
import com.gamyeon.intv.application.dto.command.UpdateIntvCommand;
import com.gamyeon.intv.application.dto.result.IntvInfo;
import com.gamyeon.intv.application.usecase.ChangeStateUseCase;
import com.gamyeon.intv.application.usecase.CreateUseCase;
import com.gamyeon.intv.application.usecase.UpdateTitleUseCase;
import com.gamyeon.intv.domain.IntvSuccessCode;
import com.gamyeon.intv.presentation.dto.request.IntvRequest;
import com.gamyeon.intv.presentation.dto.response.IntvResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/intvs")
@Slf4j
public class IntvController {

  private final CreateUseCase createUseCase;
  private final ChangeStateUseCase changeStateUseCase;
  private final UpdateTitleUseCase updateTitleUseCase;

  public IntvController(
      CreateUseCase createUseCase,
      ChangeStateUseCase changeStateUseCase,
      UpdateTitleUseCase updateTitleUseCase) {
    this.createUseCase = createUseCase;
    this.changeStateUseCase = changeStateUseCase;
    this.updateTitleUseCase = updateTitleUseCase;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<IntvResponse>> create(
      Long userId, @Valid @RequestBody IntvRequest request) {
    userId = 1L;
    log.info("Received create intv request. userId={}, title={}", userId, request.title());
    IntvInfo info = createUseCase.create(request.toCreateCommand(userId));

    return ApiResponse.success(IntvSuccessCode.INTV_CREATED, IntvResponse.from(info));
  }

  @PatchMapping("/{intvId}")
  public ResponseEntity<ApiResponse<IntvResponse>> update(
      Long userId, @PathVariable Long intvId, @Valid @RequestBody IntvRequest request) {
    userId = 1L;
    log.info(
        "Received update intv request. userId={}, intvId={}, title={}",
        userId,
        intvId,
        request.title());
    IntvInfo info = updateTitleUseCase.updateTitle(request.toUpdateCommand(userId, intvId));

    updateTitleUseCase.updateTitle(new UpdateIntvCommand(userId, intvId, request.title()));

    return ApiResponse.success(IntvSuccessCode.INTV_UPDATED, IntvResponse.from(info));
  }

  @PatchMapping("/{intvId}/start")
  public ResponseEntity<ApiResponse<Void>> start(Long userId, @PathVariable Long intvId) {
    userId = 1L;
    log.info("Received start intv request. userId={}, intvId={}", userId, intvId);
    changeStateUseCase.start(new ChangeStateIntvCommand(userId, intvId));

    return ApiResponse.success(IntvSuccessCode.INTV_STARTED);
  }

  @PatchMapping("/{intvId}/pause")
  public ResponseEntity<ApiResponse<Void>> pauseIntv(Long userId, @PathVariable Long intvId) {
    userId = 1L;
    log.info("Received pause intv request. userId={}, intvId={}", userId, intvId);
    changeStateUseCase.pause(new ChangeStateIntvCommand(userId, intvId));

    return ApiResponse.success(IntvSuccessCode.INTV_PAUSED);
  }

  @PatchMapping("/{intvId}/resume")
  public ResponseEntity<ApiResponse<Void>> resumeIntv(Long userId, @PathVariable Long intvId) {
    userId = 1L;
    log.info("Received resume intv request. userId={}, intvId={}", userId, intvId);
    changeStateUseCase.resume(new ChangeStateIntvCommand(userId, intvId));

    return ApiResponse.success(IntvSuccessCode.INTV_RESUMED);
  }

  @PatchMapping("/{intvId}/finish")
  public ResponseEntity<ApiResponse<Void>> finishIntv(Long userId, @PathVariable Long intvId) {
    userId = 1L;
    log.info("Received finish intv request. userId={}, intvId={}", userId, intvId);
    changeStateUseCase.finish(new ChangeStateIntvCommand(userId, intvId));

    return ApiResponse.success(IntvSuccessCode.INTV_FINISHED);
  }
}
