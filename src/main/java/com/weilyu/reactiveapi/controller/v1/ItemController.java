package com.weilyu.reactiveapi.controller.v1;


import com.weilyu.reactiveapi.document.Item;
import com.weilyu.reactiveapi.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ItemController {

    private final ItemReactiveRepository itemReactiveRepository;

    public ItemController(ItemReactiveRepository itemReactiveRepository) {
        this.itemReactiveRepository = itemReactiveRepository;
    }

    @GetMapping("/items")
    public Flux<Item> getAllItems() {
       return itemReactiveRepository.findAll();
    }
}
