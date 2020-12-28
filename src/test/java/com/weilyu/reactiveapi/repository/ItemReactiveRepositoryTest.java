package com.weilyu.reactiveapi.repository;

import com.weilyu.reactiveapi.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;


@DataMongoTest
class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(
            new Item(null, "Samsung TV", 400.00),
            new Item(null, "LG TV", 380.00),
            new Item(null, "Apple Watch", 299.99),
            new Item(null, "Beats Headphones", 149.99)
    );


    @BeforeEach
    public void setup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted Item is : " + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        // Use embedded mongo
        Flux<Item> itemFlux = itemReactiveRepository.findAll();
        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }
}