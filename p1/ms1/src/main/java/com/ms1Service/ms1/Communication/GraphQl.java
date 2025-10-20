package com.ms1Service.ms1.Communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.ms1Service.ms1.DbPart.dbImpl;
import com.ms1Service.ms1.DbPart.dbService;
import com.ms1Service.ms1.LlmPart.llmService;

@Controller
public class GraphQl {

    private final dbService dbService;
    private final llmService llmService;

    @Autowired
    public GraphQl(dbImpl dbService, llmService llmService) {
        this.dbService = dbService;
        this.llmService = llmService;
    }

    @QueryMapping
    public String isAlive() {
        return "ms1 is alive! viva figas \n\n\n\n";
    }


    @QueryMapping
    public String requestStock(@Argument String symbol) {
        System.out.println("Received symbol: " + symbol);
        return dbService.saveRedisData(symbol);
    }

    @QueryMapping
    public String returnLLM(){
        System.out.println("Received request for LLM analysis.");
        return llmService.getAnalysis();
    }

    @QueryMapping
    public String stopRedis(){
        return dbService.stopRedis();
    }

    @QueryMapping
    public String IAisAlive() {
        return llmService.getSummary();
    }

}