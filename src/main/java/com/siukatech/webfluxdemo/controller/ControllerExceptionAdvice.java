package com.siukatech.webfluxdemo.controller;

import com.siukatech.webfluxdemo.exception.AwsS3Exception;
import com.siukatech.webfluxdemo.model.ErrorResp;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler(AwsS3Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResp> exceptions(Throwable e) {
        ErrorResp errorResp = new ErrorResp();
        errorResp.setMessage(e.getMessage());
        errorResp.setStackTrace(NestedExceptionUtils.buildMessage(e.getMessage(), e));
        return Mono.just(errorResp);
    }


}
