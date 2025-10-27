package com.reativaMVCIA.mvc1.Communication;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public class Input {
    private List<String> topics;
    private List<Double> stocks;


    @Value("${spring.ai.openai.model}")
    private String model = "gpt-4o-mini";

    @Value("${spring.ai.openai.apiKey}")
    private String apiKey;

    // Constructors
    public Input() {}

    public Input(List<String> topics, List<Double> stocks, String model, String apiKey) {
        this.topics = topics;
        this.stocks = stocks;
        this.model = model;
        this.apiKey = apiKey;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}