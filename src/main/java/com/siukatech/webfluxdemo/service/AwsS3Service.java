package com.siukatech.webfluxdemo.service;

import com.siukatech.webfluxdemo.exception.AwsS3Exception;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AwsS3Service {

    public String putObject(String fileName, byte[] content) throws AwsS3Exception {
        UUID uuid = UUID.randomUUID();
        try {
            log.debug("putObject - fileName: [{}], content.length: [{}]", fileName, content.length);
            //System.out.println("putObject - fileName: [" + fileName
            //        + "], content.length: [" + content.length + "]");
        } catch (Exception e) {
            throw new AwsS3Exception(e.getMessage(), e);
        }
        return uuid.toString();
    }
}
