package com.llm.llmReativa.Communication;
//package main.java.com.llm.llmReativa.Communication;

import java.util.List;

public class AnalysisRequest {
    private List<String> topics;
    private List<Double> stocks;
    private String model = "gpt-4o-mini"; 

    // Constructors
    public AnalysisRequest() {}

    public AnalysisRequest(List<String> topics, List<Double> stocks, String model) {
        this.topics = topics;
        this.stocks = stocks;
        this.model = model;
    }

    // Getters and setters
    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<Double> getStocks() {
        return stocks;
    }

    public void setStocks(List<Double> stocks) {
        this.stocks = stocks;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}