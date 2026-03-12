package com.gamyeon.common.response;

import org.springframework.http.HttpStatus;

public class TempCode implements SuccessCode{

    @Override
    public String getCode() {
        return "TEMP";
    }

    @Override
    public String getMessage() {
        return "임시 메시지";
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.OK;
    }
}
