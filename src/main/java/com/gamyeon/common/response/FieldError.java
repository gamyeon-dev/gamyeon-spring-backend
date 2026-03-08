package com.gamyeon.common.response;

public class FieldError {

    private final String field;
    private final String reason;

    private FieldError(String field, String reason) {
        this.field = field;
        this.reason = reason;
    }

    public static FieldError of(String field, String reason) {
        return new FieldError(field, reason);
    }

    public String getField() {
        return field;
    }

    public String getReason() {
        return reason;
    }
}
