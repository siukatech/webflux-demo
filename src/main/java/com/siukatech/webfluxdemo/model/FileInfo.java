package com.siukatech.webfluxdemo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileInfo {
    private String filename;
    private byte[] content;
}
