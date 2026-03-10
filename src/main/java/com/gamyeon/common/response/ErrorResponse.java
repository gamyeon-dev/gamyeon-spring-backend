package com.gamyeon.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gamyeon.common.exception.ErrorCode;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private static final String DEFAULT_VALIDATION_CODE = "CMMN-V001";
    private static final String DEFAULT_VALIDATION_MESSAGE = "입력값 유효성 검사에 실패했습니다.";

    private final boolean success;
    private final String code;
    private final String message;
    private final List<FieldError> errors;

    private ErrorResponse(boolean success, String code, String message, List<FieldError> errors) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(false, errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(false, code, message, null);
    }

    public static ErrorResponse validation(List<FieldError> errors) {
        return new ErrorResponse(false, DEFAULT_VALIDATION_CODE, DEFAULT_VALIDATION_MESSAGE, errors);
    }

    public static ErrorResponse validation(String code, String message, List<FieldError> errors) {
        return new ErrorResponse(false, code, message, errors);
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

    public List<FieldError> getErrors() {
        return errors;
    }
}
