package com.siukatech.webfluxdemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileDto {
    private String filename;
    private int contentLen;
    private String uuid;
}

