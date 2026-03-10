package com.gamyeon.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.ResponseEntity;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data,
        List<ErrorDetail> errors
) {

    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode code, T data) {
        return ResponseEntity
                .status(code.getStatus())
                .body(new ApiResponse<>(
                        true,
                        code.getCode(),
                        code.getMessage(),
                        data,
                        null
                ));
    }

    public static ResponseEntity<ApiResponse<Void>> success(SuccessCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(new ApiResponse<>(
                        true,
                        code.getCode(),
                        code.getMessage(),
                        null,
                        null
                ));
    }

    public static ResponseEntity<ApiResponse<Void>> fail(ErrorCode code) {
        return ResponseEntity
                .status(code.getStatus())
                .body(new ApiResponse<>(
                        false,
                        code.getCode(),
                        code.getMessage(),
                        null,
                        null
                ));
    }

    public static ResponseEntity<ApiResponse<Void>> fail(ErrorCode code, List<ErrorDetail> errors) {
        return ResponseEntity
                .status(code.getStatus())
                .body(new ApiResponse<>(
                        false,
                        code.getCode(),
                        code.getMessage(),
                        null,
                        errors
                ));
    }
}
