package com.gamyeon.report.infrastructure.web;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.report.application.exception.ReportSuccessCode;
import com.gamyeon.report.application.port.in.HandleReportCallbackUseCase;
import com.gamyeon.report.infrastructure.external.dto.AiReportCallbackRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/internal/v1/reports")
@RequiredArgsConstructor
public class ReportCallbackController {

  private final HandleReportCallbackUseCase handleReportCallbackUseCase;

  @PostMapping("/callback")
  public ResponseEntity<ApiResponse<Void>> callback(@RequestBody AiReportCallbackRequest request) {
    log.info(
        "[Report] AI Callback 수신 - intvId={}, status={}", request.getIntvId(), request.getStatus());
    handleReportCallbackUseCase.handle(request);
    return ApiResponse.success(ReportSuccessCode.REPORT_CALLBACK_SUCCESS);
  }
}
