package com.reativaMVC1.mvc1.Communication;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.reativaMVC1.mvc1.Entity.StockEntity;
import com.reativaMVC1.mvc1.Redis.RedisImperativeService;
import com.reativaMVC1.mvc1.Service.StocksService;

@Controller
public class GraphQl {
    
    private final StocksService stocksService;
    private final RedisImperativeService redisService;


    public GraphQl(StocksService stocksService, RedisImperativeService redisService) {
        this.stocksService = stocksService;
        this.redisService = redisService;
    }

    @QueryMapping
    public StockEntity getStock(@Argument int id){
        return stocksService.findById(id);
    }

    @QueryMapping 
    public String requestStocking(@Argument String name){
        redisService.recordStockRequest(name);
        return "Request recorded for stock: " + name;
    }

    @QueryMapping
    public List<String> giveMeTop2Stocks(){
        return redisService.getTop2Request();
    }



    @MutationMapping
    public StockEntity createStock(@Argument String stockName){
        return stocksService.createAllStock(stockName);
    }

    @QueryMapping
    public List<StockEntity> giveToAIStocks1(){
        return stocksService.giveBackStock1();
    }

    

    @QueryMapping
    public List<StockEntity> giveToAIStocks2 (){
        return stocksService.giveBackStock2();
    }

    @QueryMapping
    public String giveBackToAIPrompt(){
        /*
         * Send back to ms1 the data News + stocks data 
         */

        return stocksService.giveBackPrompt();

    }

    // TODO more methods

}