package com.gamyeon.feedback.infrastructure.web;

import com.gamyeon.feedback.application.exception.QuestionSetNotFoundException;
import com.gamyeon.feedback.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice(basePackages = "com.gamyeon.feedback")
public class FeedbackExceptionHandler {

  @ExceptionHandler(QuestionSetNotFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleNotFound(QuestionSetNotFoundException ex) {
    log.warn("[Feedback] 리소스 없음: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.failure("FDBK-N001", "평가 결과를 찾을 수 없습니다."));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidation(IllegalArgumentException ex) {
    log.warn("[Feedback] 입력값 오류: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.failure("FDBK-V001", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleInternal(Exception ex) {
    log.error("[Feedback] 서버 내부 오류", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.failure("FDBK-I001", "서버 내부 오류가 발생했습니다."));
  }
}
