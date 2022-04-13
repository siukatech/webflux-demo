package com.siukatech.webfluxdemo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResp {
    private String message;
    private String stackTrace;
}
