package com.devjoemar.supportengineerscheduling.exception;

import com.devjoemar.supportengineerscheduling.api.ApiResponse;
import com.devjoemar.supportengineerscheduling.util.Constant;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(Exception ex) {
        log.error("Exception ", ex);
        ApiResponse<String> ApiResponse = com.devjoemar.supportengineerscheduling.api.ApiResponse.<String>builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(Constant.RESULT_NOT_OK)
                .message(ex.getMessage())
                .internalCode(Constant.GENERIC_RESPONSE_CODE)
                .data(null)
                .build();

        return new ResponseEntity<>(ApiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(Exception ex) {
        log.error("IllegalArgumentException ", ex);
        ApiResponse<String> ApiResponse = com.devjoemar.supportengineerscheduling.api.ApiResponse.<String>builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .status(Constant.RESULT_NOT_OK)
                .message(ex.getMessage())
                .internalCode(Constant.RESPONSE_CODE_PREFIX.concat("11"))
                .data(null)
                .build();

        return new ResponseEntity<>(ApiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}