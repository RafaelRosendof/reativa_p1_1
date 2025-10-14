package com.ms1Service.ms1.Communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import com.ms1Service.ms1.DbPart.dbImpl;
import com.ms1Service.ms1.DbPart.dbService;
import com.ms1Service.ms1.LlmPart.llmService;

public class GraphQl {

    private final dbService dbService;
    private final llmService llmService;

    @Autowired
    public GraphQl(dbImpl dbService, llmService llmService) {
        this.dbService = dbService;
        this.llmService = llmService;
    }


    @QueryMapping
    public String requestStock(String symbol) {
        System.out.println("Received symbol: " + symbol);
        return dbService.saveRedisData(symbol);
    }

    @QueryMapping
    public String returnLLM(){
        return llmService.getAnalysis();
    }

    @QueryMapping
    public String stopRedis(){
        return dbService.stopRedis();
    }

}