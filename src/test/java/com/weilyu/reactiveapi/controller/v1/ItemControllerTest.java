package com.weilyu.reactiveapi.controller.v1;

import com.weilyu.reactiveapi.document.Item;
import com.weilyu.reactiveapi.repository.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@DirtiesContext
// It indicates the associated test or class modifies the ApplicationContext. It tells the testing framework to close and recreate the context for later tests.
@AutoConfigureWebTestClient   // Annotation that can be applied to a test class to enable a WebTestClient.
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item> data() {
        return Arrays.asList(
                new Item(null, "Samsung TV", 399.99),
                new Item(null, "LG TV", 329.99),
                new Item(null, "Apple Watch", 349.99),
                new Item(null, "Beats HeadPhones", 149.99));
    }

    @BeforeEach
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(item -> itemReactiveRepository.save(item))
                .doOnNext(item -> System.out.println("Inserted item is : " + item))
                .blockLast();
    }

    @Test
    void getAllItems() {
        webTestClient.get().uri("/api/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    void getAllItems_approach2() {
        webTestClient.get().uri("/api/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class) // List<Item>
                .hasSize(4)
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    assert items != null;
                    items.forEach(item -> assertNotNull(item.getId()));
                });
    }

    @Test
    void getAllItems_approach3() {
        Flux<Item> itemsFlux = webTestClient.get().uri("/api/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class) // Returns a Flux<Item>
                .getResponseBody();

        StepVerifier.create(itemsFlux.log())
                .expectNextCount(4)
                .verifyComplete();
    }
}