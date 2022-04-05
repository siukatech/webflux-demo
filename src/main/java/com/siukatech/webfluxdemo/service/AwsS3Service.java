package com.siukatech.webfluxdemo.service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AwsS3Service {

    public String putObject(String fileName, byte[] content) {
        UUID uuid = UUID.randomUUID();
        log.debug("putObject - fileName: [{}], content.length: [{}]", fileName, content.length);
        //System.out.println("putObject - fileName: [" + fileName
        //        + "], content.length: [" + content.length + "]");
        return uuid.toString();
    }
}
