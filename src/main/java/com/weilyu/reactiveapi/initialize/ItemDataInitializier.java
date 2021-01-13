package com.weilyu.reactiveapi.initialize;


import com.weilyu.reactiveapi.document.Item;
import com.weilyu.reactiveapi.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
public class ItemDataInitializier implements CommandLineRunner {

    private final ItemReactiveRepository itemReactiveRepository;

    public ItemDataInitializier(ItemReactiveRepository itemReactiveRepository) {
        this.itemReactiveRepository = itemReactiveRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
    }

    public List<Item> data() {
        return Arrays.asList(
                new Item(null, "Samsung TV", 399.99),
                new Item(null, "LG TV", 329.99),
                new Item(null, "Apple Watch", 349.99),
                new Item("abc", "Beats HeadPhones", 149.99));
    }

    private void initialDataSetup() {
        itemReactiveRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> log.info("Item inserted from CommandLineRunner"));
    }
}
