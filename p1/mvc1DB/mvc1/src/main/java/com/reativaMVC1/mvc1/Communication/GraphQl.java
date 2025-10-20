package com.reativaMVC1.mvc1.Communication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


import com.reativaMVC1.mvc1.Entity.StockEntity;
import com.reativaMVC1.mvc1.Redis.RedisImperativeService;
import com.reativaMVC1.mvc1.Service.NewsService;
import com.reativaMVC1.mvc1.Service.StocksService;

@Controller
public class GraphQl {

    private final NewsService newsService;
    
    private final StocksService stocksService;
    private final RedisImperativeService redisService;

    @Autowired
    public GraphQl(StocksService stocksService, RedisImperativeService redisService, NewsService newsService) {
        this.stocksService = stocksService;
        this.redisService = redisService;
        this.newsService = newsService;
    }



    @QueryMapping 
    public String requestStocking(@Argument String name){
        redisService.recordStockRequest(name);
        return "Request recorded for stock: " + name;
    }


    @QueryMapping
    public String stopRedis(){
        System.out.println("\n\n Received stopRedis request in GraphQl controller. \n\n");
        return stocksService.stopRedis();
    }


    @QueryMapping
    public String giveBackToAIPrompt1(){
        return stocksService.giveBackPrompt1();
    }

    @QueryMapping
    public String giveBackToAIPrompt2(){
        return stocksService.giveBackPrompt2();
    }

}
