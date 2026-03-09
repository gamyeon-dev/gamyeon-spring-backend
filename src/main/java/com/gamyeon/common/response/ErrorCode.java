package com.gamyeon.common.response;

import org.springframework.http.HttpStatus;

public interface ErrorCode extends BaseCode {

    HttpStatus getStatus();

}