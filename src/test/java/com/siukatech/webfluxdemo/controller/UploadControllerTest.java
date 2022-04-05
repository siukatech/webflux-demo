package com.siukatech.webfluxdemo.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest
public class UploadControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void uploadGet() {
        Flux<String> stringFlux = webTestClient.get()
                //.uri("/upload-get?name=abc")
                .uri(uriBuilder -> uriBuilder
                        .path("/upload-get")
                        //.query("name=abc")
                        .queryParam("name", "abc")
                        //.queryParam("nameParam", "def")
                        .build()
                )
                //.accept(MediaType.ALL)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody()
                ;
        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("abc")
                .verifyComplete()
                ;
    }

}
