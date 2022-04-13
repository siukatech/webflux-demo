package com.siukatech.webfluxdemo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

@Getter
@Setter
@NoArgsConstructor
public class FileFlux {
    private String filename;
    private Flux<DataBuffer> dataBufferFlux;
}
