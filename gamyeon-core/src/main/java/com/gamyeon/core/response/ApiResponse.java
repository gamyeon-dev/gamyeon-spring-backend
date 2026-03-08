package com.gamyeon.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gamyeon.core.exception.ErrorCode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private static final String SUCCESS_CODE = "S000";
    private static final String VALIDATION_CODE = "V001";

    private final boolean success;
    private final String code;
    private final String message;
    private final T data;
    private final List<FieldError> errors;

    private ApiResponse(boolean success, String code, String message, T data, List<FieldError> errors) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, SUCCESS_CODE, "success", data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, SUCCESS_CODE, message, data, null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null, null);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(false, code, message, null, null);
    }

    public static <T> ApiResponse<T> validationFail(List<FieldError> errors) {
        return new ApiResponse<>(false, VALIDATION_CODE, "입력값 유효성 검사에 실패했습니다.", null, errors);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
