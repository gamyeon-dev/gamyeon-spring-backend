package com.gamyeon.common.response;

public record ErrorDetail(
        String field,
        String reason
) {

    public static ErrorDetail of(String field, String reason) {
        return new ErrorDetail(field, reason);
    }

}