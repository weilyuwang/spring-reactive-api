package com.weilyu.reactiveapi.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest // Need to add this in order to pick up RouterFunction Bean
@AutoConfigureWebTestClient // Need to add this to autowire WebTestClient
@DirtiesContext
class SampleHandlerFunctionTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void fluxTest() {
        Flux<Integer> integerFlux = webTestClient.get().uri("/functional/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();
    }



    @Test
    void monoTest() {
        Integer expectedValue = 1;

        webTestClient.get().uri("/functional/mono")
                .accept(MediaType.APPLICATION_JSON)
                .exchange() // exchange() needs to be called in order to connect to the endpoint
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith(response -> {
                    assertEquals(expectedValue, response.getResponseBody());
                });
    }
}