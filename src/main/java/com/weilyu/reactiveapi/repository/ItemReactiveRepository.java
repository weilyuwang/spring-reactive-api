package com.weilyu.reactiveapi.repository;

import com.weilyu.reactiveapi.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
}
