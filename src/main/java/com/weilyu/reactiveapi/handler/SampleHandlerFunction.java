package com.weilyu.reactiveapi.handler;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component // We want this class to be scanned as a bean
public class SampleHandlerFunction {
    // When you use RouterFunction the response type is always going to be Mono<ServerResponse>.
    public Mono<ServerResponse> fluxHandler(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.just(1, 2, 3, 4).log(), Integer.class);
    }

    public Mono<ServerResponse> monoHandler(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(1).log(), Integer.class);
    }
}
