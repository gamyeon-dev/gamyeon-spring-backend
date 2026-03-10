package com.gamyeon.common.response;

public record FieldError(String field, String reason) {

    public static FieldError of(String field, String reason) {
        return new FieldError(field, reason);
    }
}
