package com.siukatech.webfluxdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
//@AllArgsConstructor
public class UploadResp {
    private List<FileDto> uploadedFiles;
    private String exceptionMsg;
}

