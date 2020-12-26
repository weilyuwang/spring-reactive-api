package com.weilyu.reactiveapi.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Expected Error")))
                .concatWith(Flux.just("After Error - This will not be showed"))
                .log();

        stringFlux.subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("Completed - This will not be showed either")
        );
    }
}
