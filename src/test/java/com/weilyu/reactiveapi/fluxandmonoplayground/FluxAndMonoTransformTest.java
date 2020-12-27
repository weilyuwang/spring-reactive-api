package com.weilyu.reactiveapi.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {
    List<String> names = Arrays.asList("Adam", "Anna", "Jack", "Jerry");

    @Test
    void transformUsingMap() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("ADAM", "ANNA", "JACK", "JERRY")
                .verifyComplete();
    }


    @Test
    void transformUsingMapLengthRepeat() {
        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(String::length)
                .repeat(1)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext(4, 4, 4, 5, 4, 4, 4, 5)
                .verifyComplete();
    }

    @Test
    void transformUsingMapFilter() {
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("JERRY")
                .verifyComplete();
    }


    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }


    @Test
    void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .map(this::convertToList) // Flux<List<String>>
                .flatMap(Flux::fromIterable) // flat Flux<Flux<String>> into Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();  // Took 6 seconds
    }


    @Test
    void transformUsingFlatMapUsingParallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) // Flux<String>
                .window(2) // Wait until two elements, Flux<Flux<String>> -> (A, B), (C, D), (E, F)
                .flatMap(fluxOfString ->
                        fluxOfString
                                .map(this::convertToList) // flat Flux<Flux<List<String>>> into Flux<List<String>>
                                .subscribeOn(parallel())) // Flux<List<String>>
                .flatMap(Flux::fromIterable) // flat Flux<Flux<String>> into Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();   // Took 3 seconds, but order not maintained
    }


    @Test
    void transformUsingFlatMapUsingParallelMaintainOrder() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) // Flux<String>
                .window(2) // Wait until two elements, Flux<Flux<String>> -> (A, B), (C, D), (E, F)
                .flatMapSequential(fluxOfString ->
                        fluxOfString
                                .map(this::convertToList) // flat Flux<Flux<List<String>>> into Flux<List<String>>
                                // When you call subscribeOn(Schedulers.parallel()).
                                // You specify that you want to receive items on the different thread
                                .subscribeOn(parallel())) // Flux<List<String>>
                .flatMap(Flux::fromIterable) // flat Flux<Flux<String>> into Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();   // Took 2 seconds, order maintained
    }

}
