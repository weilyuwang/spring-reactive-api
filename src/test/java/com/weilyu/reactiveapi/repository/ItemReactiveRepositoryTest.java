package com.weilyu.reactiveapi.repository;

import com.weilyu.reactiveapi.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
            new Item("123", "Beats Headphones", 149.99)
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
    void getAllItems() {
        // Use embedded mongo
        Flux<Item> itemFlux = itemReactiveRepository.findAll();
        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    void getItemById() {
        Mono<Item> itemMono = itemReactiveRepository.findById("123");
        StepVerifier.create(itemMono)
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Beats Headphones"))
                .verifyComplete();
    }

    @Test
    void findByDescription() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Samsung TV"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void saveItem() {
        Item itemToSave = new Item(null, "Google Home Mini", 30.00);
        Mono<Item> savedItem = itemReactiveRepository.save(itemToSave);
        StepVerifier.create(savedItem.log("savedItem: "))
                .expectSubscription()
                .expectNextMatches(item -> item.getId() != null && item.getDescription().equals("Google Home Mini"))
                .verifyComplete();
    }

    @Test
    void updateItem() {
        double newPrice = 520.00;

        Flux<Item> updatedItem = itemReactiveRepository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(newPrice);
                    return item;
                })
                .flatMap(item -> itemReactiveRepository.save(item)); // Flux<Mono<Item>> --> Flux<Item>

        StepVerifier.create(updatedItem.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 520.00)
                .verifyComplete();
    }

    @Test
    void deleteItemById() {

        Mono<Void> deletedItem = itemReactiveRepository.findById("123") // Mono<Item>
                .map(Item::getId)
                .flatMap(id -> itemReactiveRepository.deleteById(id));  // Use flatMap to flatten Mono<Mono<Void>> to Mono<Void>

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("New Item list: "))
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void deleteItem() {

        Flux<Void> deletedItem = itemReactiveRepository.findByDescription("LG TV") // Mono<Item>
                .flatMap(item -> itemReactiveRepository.delete(item));  // Use flatMap to flatten Flux<Mono<Void>> to Mono<Void>

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("New Item list: "))
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }
}