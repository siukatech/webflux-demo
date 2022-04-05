package com.siukatech.webfluxdemo.controller;

import com.siukatech.webfluxdemo.service.AwsS3Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UploadController {

    //Logger log = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private AwsS3Service awsS3Service;

    @GetMapping("/upload-get")
    public Mono<String> uploadGet(@RequestParam(value = "name") String nameParam) {
        String name = "abc";
        name = nameParam;
        log.debug("name: [{}]", name);
        return Mono.just(name);
    }

    @GetMapping(value = "/upload-get-stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> uploadGetStream() {
        Flux<String> stringFlux = Flux.just("a1", "b2", "c3", "d4")
                .log(log.getName());
        return stringFlux;
    }

    @PostMapping("/upload-test")
    public Mono<String> uploadTest(@RequestParam String name) {
        log.debug("name: [{}]", name);
        return Mono.just(name);
    }

    @PostMapping(value = "upload-file-flux-test-1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadFileMvcTest1(@RequestPart(name = "file") Flux<FilePart> filePartFlux) throws IOException {
        log.debug("This is a test");
        filePartFlux.log();
        filePartFlux.doOnNext(filePart -> log.debug("filePath.filename: [" + filePart.filename() + "]"))
                .then()
        ;
        return Mono.just("OK");
    }


    // This one is ok
    @PostMapping(value = "upload-files-test-1e", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadPostTest1e(@RequestPart(name = "files") Mono<FilePart> filePart, ServerWebExchange exchange) {
        String dirPath = "/Users/karl.hk.yeung/Documents/gt/project/Nanfung/upload-working/";
        String filenamePrefix = "flux-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-";
        return filePart.doOnNext(filePart1 -> log.debug("uploadPostTest1e - dirPath: [" + dirPath
                        + "], filenamePrefix: [" + filenamePrefix
                        + "], filePart1: [" + filePart1.filename()
                        + "]"))
                .flatMap(filePart1 -> filePart1.transferTo(Path.of(dirPath, (filenamePrefix + filePart1.filename())).toFile()))
                .then(Mono.just("OK"));
    }

    // only one flux is captured
    @PostMapping(value = "upload-files-test-1e-2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadPostTest1e2(@RequestPart(name = "files") Mono<FilePart> filePartMono, ServerWebExchange exchange) {
        String dirPath = "/Users/karl.hk.yeung/Documents/gt/project/Nanfung/upload-working/";
        String namePrefix = "flux-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-";

        //Mono<FileInfo> fileInfoMono =
        return filePartMono
                // from Mono<FilePart> to Mono<FileInfo>
                .flatMap(filePart -> {
                    String filename = filePart.filename();

                    // Mono<FileInfo>
                    return filePart.content()
                            .flatMap(dataBuffer -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                return Mono.just(bytes);
                            })
                            // from Flux<byte> to Mono<FileInfo>
                            .flatMap(bytes -> {
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.setFilename(namePrefix + filename);
                                fileInfo.setContent(bytes);
                                return Mono.just(fileInfo);
                            })
                            // from Flux<FileInfo> to Mono<FileInfo>
                            .next()
                            ;
                })
                // from Mono<FileInfo> to Mono<String>
                .map(fileInfo -> {
                    return fileInfo.getFilename() + "-" + fileInfo.getContent().length;
                })
                .doOnNext(str -> log.debug("uploadPostTest1e2 - dirPath: [" + dirPath
                        + "], filenamePrefix: [" + namePrefix
                        + "], str: [" + str
                        + "]"))
                .log()
                ;
    }

    // this one is ok
    @PostMapping(value = "upload-files-test-1e-3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> uploadPostTest1e3(@RequestPart(name = "files") Mono<FilePart> filePartMono, ServerWebExchange exchange) {
        String dirPath = "/Users/karl.hk.yeung/Documents/gt/project/Nanfung/upload-working/";
        String namePrefix = "flux-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-";

        //Mono<FileInfo> fileInfoMono =
        return filePartMono
                // from Mono<FilePart> to Mono<FileInfo>
                .log()
                .flatMap(filePart -> {
                    String filename = filePart.filename();

                    // Mono<FileInfo>
                    return filePart.content()
                            .log()
                            .collectList()
                            .log()
                            .map(dataBuffers -> {
                                DataBuffer db = DefaultDataBufferFactory.sharedInstance.join(dataBuffers);
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.setFilename(namePrefix + filename);
                                fileInfo.setContent(db.asByteBuffer().array());
                                return fileInfo;
                            })
                            .log()
                            ;
                })
                .log()
                // from Mono<FileInfo> to Mono<String>
                .map(fileInfo -> {
                    return fileInfo.getFilename() + "-" + fileInfo.getContent().length;
                })
                .log()
                .doOnNext(str -> log.debug("uploadPostTest1e3 - dirPath: [" + dirPath
                        + "], filenamePrefix: [" + namePrefix
                        + "], str: [" + str
                        + "]"))
                .log()
                ;
    }

    // this one is ok
    @PostMapping(value = "upload-files-test-1e-3b", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Flux<String> uploadPostTest1e3b(@RequestPart(name = "files") Flux<FilePart> filePartFlux, ServerWebExchange exchange) {
        String dirPath = "/Users/karl.hk.yeung/Documents/gt/project/Nanfung/upload-working/";
        String namePrefix = "flux-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-";

        //Mono<FileInfo> fileInfoMono =
        return filePartFlux
                // from Flux<FilePart> to Flux<FileInfo>
                .log()
                .flatMap(filePart -> {
                    String filename = filePart.filename();

                    // Flux<FileInfo>
                    return filePart.content()
                            //.log()
                            .collectList()
                            //.log()
                            .map(dataBuffers -> {
                                DataBuffer db = DefaultDataBufferFactory.sharedInstance.join(dataBuffers);
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.setFilename(namePrefix + filename);
                                fileInfo.setContent(db.asByteBuffer().array());
                                return fileInfo;
                            })
                            .flux()
                            //.log()
                            ;
                })
                .log()
                // from Flux<FileInfo> to Flux<String>
                .map(fileInfo -> {
                    return fileInfo.getFilename() + "-" + fileInfo.getContent().length;
                })
                .log()
                .doOnNext(str -> log.debug("uploadPostTest1e3b - dirPath: [" + dirPath
                        + "], filenamePrefix: [" + namePrefix
                        + "], str: [" + str
                        + "]"))
                .log()
                ;
    }

    // this one is ok
    @PostMapping(value = "upload-files-test-1e-3c"
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<UploadResp> uploadPostTest1e3c(@RequestPart(name = "files") Flux<FilePart> filePartFlux, ServerWebExchange exchange) {
        String dirPath = "/Users/karl.hk.yeung/Documents/gt/project/Nanfung/upload-working/";
        String namePrefix = "flux-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-";

        //Mono<FileInfo> fileInfoMono =
        return filePartFlux
                // from Flux<FilePart> to Flux<FileInfo>
                .log()
                .flatMap(filePart -> {
                    String filename = filePart.filename();

                    // Flux<FileInfo>
                    return filePart.content()
                            //.log()
                            .collectList()
                            //.log()
                            .map(dataBuffers -> {
                                DataBuffer db = DefaultDataBufferFactory.sharedInstance.join(dataBuffers);
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.setFilename(namePrefix + filename);
                                fileInfo.setContent(db.asByteBuffer().array());
                                return fileInfo;
                            })
                            .flux()
                            //.log()
                            ;
                })
                .log()
                // from Flux to Mono
                .collectList()
                // from Mono<List<FileInfo>> to Mono<UploadResp)
                .map(fileInfos -> {
                    return new UploadResp(fileInfos.stream()
                            .map(fileInfo -> new FileDto(fileInfo.getFilename(), fileInfo.getContent().length, ""))
                            .collect(Collectors.toList())
                    );
                })
                .log()
                .doOnNext(uploadResp -> {
                    log.debug("uploadPostTest1e3c - dirPath: [" + dirPath
                            + "], filenamePrefix: [" + namePrefix
                            + "]");
                    uploadResp.uploadedFileList.forEach(fileDto ->
                            log.debug("uploadPostTest1e3c - fileDto.getFilename: [" + fileDto.getFilename()
                                    + "], fileDto.getContentLen: [" + fileDto.getContentLen()
                                    + "]"));
                })
                .log()
                ;
    }

    // this one is ok
    @PostMapping(value = "upload-files-test-1e-3d"
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<UploadResp> uploadPostTest1e3d(@RequestPart(name = "files") Flux<FilePart> filePartFlux, ServerWebExchange exchange) {
        String dirPath = "/Users/karl.hk.yeung/Documents/gt/project/Nanfung/upload-working/";
        String namePrefix = "flux-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-";

        //Mono<FileInfo> fileInfoMono =
        return filePartFlux
                // from Flux<FilePart> to Flux<FileInfo>
                .log()
                .flatMap(filePart -> {
                    String filename = filePart.filename();

                    // Flux<FileInfo>
                    return filePart.content()
                            //.log()
                            .collectList()
                            //.log()
                            .map(dataBuffers -> {
                                DataBuffer db = DefaultDataBufferFactory.sharedInstance.join(dataBuffers);
                                FileInfo fileInfo = new FileInfo();
                                fileInfo.setFilename(namePrefix + filename);
                                fileInfo.setContent(db.asByteBuffer().array());
                                return fileInfo;
                            })
                            .flux()
                            //.log()
                            ;
                })
                .log()
                .map(fileInfo -> {
                    String uuid = awsS3Service.putObject(fileInfo.getFilename(), fileInfo.getContent());
                    FileDto fileDto = new FileDto(fileInfo.getFilename(), fileInfo.getContent().length, uuid);
                    return fileDto;
                })
                // from Flux to Mono
                .collectList()
                // from Mono<List<FileInfo>> to Mono<UploadResp)
                .map(fileDtos -> new UploadResp(fileDtos))
                .log()
                .doOnNext(uploadResp -> {
                    log.debug("uploadPostTest1e3c - dirPath: [" + dirPath
                            + "], filenamePrefix: [" + namePrefix
                            + "]");
                    uploadResp.uploadedFileList.forEach(fileDto ->
                            log.debug("uploadPostTest1e3c - fileDto.getFilename: [" + fileDto.getFilename()
                                    + "], fileDto.getContentLen: [" + fileDto.getContentLen()
                                    + "]"));
                })
                .log()
                ;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public class FileInfo {
        private String filename;
        private byte[] content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class FileDto {
        private String filename;
        private int contentLen;
        private String uuid;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public class UploadResp {
        private List<FileDto> uploadedFileList;
    }


}
