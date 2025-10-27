package com.reativa.part.Communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.reativa.part.Redis.RedisReactive;
import com.reativa.part.Service.NewsService;
import com.reativa.part.Service.StocksService;

import reactor.core.publisher.Mono;

@Controller
public class GraphQl {

    private final NewsService newsService;
    private final StocksService stocksService;
    private final RedisReactive redisService;

    @Autowired
    public GraphQl(NewsService newsService, StocksService stocksService, RedisReactive redisService) {
        this.newsService = newsService;
        this.stocksService = stocksService;
        this.redisService = redisService;
    }

    @QueryMapping
    public Mono<String> stopRedis(){
        System.out.println("\n\n Received stopRedis request in GraphQl controller. \n\n");
        return stocksService.stopRedis().map(result -> {
            System.out.println("Redis stopped: " + result);
            return "Redis stopped";
        });
    }

    @QueryMapping
    public Mono<String> giveBackToAIPrompt1(){
        System.out.println("\n\n Received giveBackToAIPrompt1 request in GraphQl controller. \n\n");
        return stocksService.giveBackPrompt1();
    }

    @QueryMapping
    public Mono<String> giveBackToAIPrompt2(){
        System.out.println("\n\n Received giveBackToAIPrompt2 request in GraphQl controller. \n\n");
        return stocksService.giveBackPrompt2();
    }   
}