// package com.gamyeon.report.presentation;
//
// import com.gamyeon.common.response.SuccessResponse;
// import com.gamyeon.feedback.application.port.in.FeedbackCallbackUseCase; // Feedback용 Port
// import com.gamyeon.feedback.infrastructure.web.dto.FeedbackCallbackRequest;
// import com.gamyeon.report.application.port.in.ReportCallbackUseCase; // Callback 처리용 Port
// import com.gamyeon.report.presentation.dto.request.ReportWebhookRequest;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.*;
//
// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/internal/v1")
// public class InternalReportController {
//
//  private final ReportCallbackUseCase reportCallbackUseCase;
//  private final FeedbackCallbackUseCase feedbackCallbackUseCase;
//
//  /** [AI -> BE] 피드백 생성 완료 Callback POST /internal/v1/feedbacks/callback */
//  @PostMapping("/feedbacks/callback")
//  public SuccessResponse<Void> handleFeedbackCallback(
//      @RequestBody FeedbackCallbackRequest request) {
//
//    feedbackCallbackUseCase.processFeedbackCallback(request);
//    return SuccessResponse.of(null); // 데이터 없이 성공 응답만 반환
//  }
//
//  /** [AI -> BE] 리포트 생성 완료 Callback POST /internal/v1/reports/callback */
//  @PostMapping("/reports/callback")
//  public SuccessResponse<Void> handleReportCallback(@RequestBody ReportWebhookRequest request) {
//
//    reportCallbackUseCase.processReportCallback(request);
//    return SuccessResponse.of(null);
//  }
// }
