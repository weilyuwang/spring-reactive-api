package com.weilyu.reactiveapi.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import reactor.core.Exceptions.*;
import java.time.Duration;
import java.time.LocalTime;

public class FluxAndMonoErrorTest {

    @Test
    void fluxErrorHandling() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e -> {
                    // This block will be executed
                    System.out.println("Exception is : " + e);
                    return Flux.just("default", "default1");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
//                .expectError()
//                .verify();
                .expectNext("default", "default1")
                .verifyComplete();
    }

    @Test
    void fluxErrorHandlingOnErrorReturn() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void fluxErrorHandlingOnErrorMap() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }


    @Test
    void fluxErrorHandlingOnErrorMapWithRetry() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new)
                .retry(2); // Retry when encounters exception

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C") // Retry #1
                .expectNext("A", "B", "C") // Retry #2
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void fluxErrorHandlingOnErrorMapWithRetryBackoff() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap(CustomException::new)
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(3)));   // Retry With Backoff

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C") // Retry #1
                .expectNext("A", "B", "C") // Retry #2
                .expectError() // Retry exhausted
                .verify();
    }
}
