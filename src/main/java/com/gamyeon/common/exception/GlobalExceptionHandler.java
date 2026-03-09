package com.gamyeon.common.exception;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.common.response.CommonErrorCode;
import com.gamyeon.common.response.ErrorDetail;
import com.gamyeon.common.response.ErrorCode;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ApiResponse.fail(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        List<ErrorDetail> errors = bindingResult.getFieldErrors()
                .stream()
                .map(error -> ErrorDetail.of(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return ApiResponse.fail(CommonErrorCode.INVALID_INPUT_VALUE, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ApiResponse.fail(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}