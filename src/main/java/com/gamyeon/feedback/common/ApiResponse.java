package com.gamyeon.feedback.common;

public record ApiResponse<T>(boolean success, String code, String message, T data) {
  public static <T> ApiResponse<T> success(String code, String message) {
    return new ApiResponse<>(true, code, message, null);
  }

  public static <T> ApiResponse<T> failure(String code, String message) {
    return new ApiResponse<>(false, code, message, null);
  }
}
