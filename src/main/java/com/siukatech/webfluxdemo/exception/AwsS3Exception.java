package com.siukatech.webfluxdemo.exception;

public class AwsS3Exception extends RuntimeException {
    public AwsS3Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
