package com.weilyu.reactiveapi.router;


import com.weilyu.reactiveapi.handler.SampleHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class RouterFunctionConfig {
    // Responsible for routing incoming requests to different handler functions

    @Bean
    public RouterFunction<ServerResponse> route(SampleHandlerFunction handlerFunction) {
        return RouterFunctions
                .route(GET("/functional/flux"), handlerFunction::fluxHandler)
                .andRoute(GET("/functional/mono"), handlerFunction::monoHandler);
    }

}
