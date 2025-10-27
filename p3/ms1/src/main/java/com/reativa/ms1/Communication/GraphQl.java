package com.reativa.ms1.Communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.reativa.ms1.Dbpart.dbService;
import com.reativa.ms1.LlmPart.llmService;

import reactor.core.publisher.Mono;

@Controller

public class GraphQl {

    private final dbService dbservice;
    private final llmService llmservice;

    @Autowired
    public GraphQl(dbService dbservice, llmService llmservice) {
        this.dbservice = dbservice;
        this.llmservice = llmservice;
    }

    @QueryMapping
    public Mono<String> isAlive(){
        System.out.println("\n\n Received isAlive request in GraphQl controller. \n\n");
        return Mono.just("MS1 is alive");
    }

    @QueryMapping
    public Mono<String> requestStock(@Argument String symbol){
        System.out.println("\n\n Received requestStock request in GraphQl controller. \n\n");
        return dbservice.saveRedisData(symbol);
    }

    @QueryMapping
    public Mono<String> stopRedis(){
        System.out.println("\n\n Received stopRedis request in GraphQl controller. \n\n");
        return dbservice.stopRedis().map(result -> {
            System.out.println("Redis stopped: " + result);
            return "Redis stopped";
        });
    }
}