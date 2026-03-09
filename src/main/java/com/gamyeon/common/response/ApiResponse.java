package com.gamyeon.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(

        boolean success,
        String code,
        String message,
        T data,
        List<ErrorDetail> errors

) {

    public static <T> ApiResponse<T> success(SuccessCode code, T data) {
        return new ApiResponse<>(
                true,
                code.getCode(),
                code.getMessage(),
                data,
                null
        );
    }

    public static ApiResponse<Void> success(SuccessCode code) {
        return new ApiResponse<>(
                true,
                code.getCode(),
                code.getMessage(),
                null,
                null
        );
    }

    public static ApiResponse<Void> fail(ErrorCode code) {
        return new ApiResponse<>(
                false,
                code.getCode(),
                code.getMessage(),
                null,
                null
        );
    }

    public static ApiResponse<Void> fail(ErrorCode code, List<ErrorDetail> errors) {
        return new ApiResponse<>(
                false,
                code.getCode(),
                code.getMessage(),
                null,
                errors
        );
    }

}