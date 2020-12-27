package com.weilyu.reactiveapi.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jerry");

    @Test
    void fluxUsingIterable() {
        Flux<String> namesFlux = Flux.fromIterable(names).log();

        StepVerifier.create(namesFlux)
                .expectNext("Adam", "Anna", "Jack", "Jerry")
                .verifyComplete();
    }

    @Test
    void fluxUsingArray() {
        // Note that an array is not a iterable in Java
        String[] names = new String[]{"Adam", "Anna", "Jack", "Jerry"};

        Flux<String> namesFlux = Flux.fromArray(names).log();

        StepVerifier.create(namesFlux)
                .expectNext("Adam", "Anna", "Jack", "Jerry")
                .verifyComplete();
    }

    @Test
    void fluxUsingStream() {
        Flux<String> namesFlux = Flux.fromStream(names.stream()).log();

        StepVerifier.create(namesFlux)
                .expectNext("Adam", "Anna", "Jack", "Jerry")
                .verifyComplete();
    }


    @Test
    void monoUsingJustOrEmpty() {
        Mono<String> mono = Mono.justOrEmpty(null); // Mono.Empty()

        StepVerifier.create(mono.log()).verifyComplete();
    }

    @Test
    void monoUsingSupplier() {
        Supplier<String> stringSupplier = () -> "Adam";
        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

        // To get the value out of a supplier:
        System.out.println(stringSupplier.get());

        StepVerifier.create(stringMono.log())
                .expectNext("Adam")
                .verifyComplete();
    }

    @Test
    void fluxUsingRange() {
        Flux<Integer> integerFlux = Flux.range(1, 5).log();

        StepVerifier.create(integerFlux)
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }
}
