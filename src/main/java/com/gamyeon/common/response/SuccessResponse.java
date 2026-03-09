package com.gamyeon.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {

    private static final String SUCCESS_CODE = "S000";

    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    private SuccessResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(true, SUCCESS_CODE, "success", data);
    }

    public static <T> SuccessResponse<T> of(String message, T data) {
        return new SuccessResponse<>(true, SUCCESS_CODE, message, data);
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
}
