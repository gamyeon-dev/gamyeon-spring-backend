package com.gamyeon.common.response;

import org.springframework.http.HttpStatus;

public interface SuccessCode extends BaseCode {

  HttpStatus getStatus();
}
