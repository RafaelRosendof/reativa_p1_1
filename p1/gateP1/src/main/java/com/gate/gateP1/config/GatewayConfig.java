package com.gate.gateP1.config;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunctions() {
        return GatewayRouterFunctions.route("ms1-graphql")
                .route(RequestPredicates.path("/graphql"), 
                       HandlerFunctions.http("lb://MS1"))
                .build()
            .and(GatewayRouterFunctions.route("mvc1-database")
                .route(RequestPredicates.path("/api/database/**"), 
                       HandlerFunctions.http("lb://MVC1"))
                .build())
            .and(GatewayRouterFunctions.route("mvc1ia-ai")
                .route(RequestPredicates.path("/api/ai/**"), 
                       HandlerFunctions.http("lb://MVC1IA"))
                .build());
    }
}