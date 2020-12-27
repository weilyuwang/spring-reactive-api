package com.weilyu.reactiveapi.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    void backPressureTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(2)
                .expectNext(2)
                .thenCancel()
                .verify();
    }


    @Test
    void backPressure() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(element -> System.out.println("Element is : " + element),
                e -> System.err.println("Exception is : " + e),
                () -> System.out.println("Done"),
                subscription -> subscription.request(2)
        );
    }

    @Test
    void backPressureCancel() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(element -> System.out.println("Element is : " + element),
                e -> System.err.println("Exception is : " + e),
                () -> System.out.println("Done"),
                subscription -> subscription.cancel()
        );
    }

    @Test
    void customizedBackPressure() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(
                new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnNext(Integer value) {
                        request(1);
                        System.out.println("Value received is : " + value);
                        if(value == 4) {
                            cancel();
                        }
                    }
                }
        );
    }
}
