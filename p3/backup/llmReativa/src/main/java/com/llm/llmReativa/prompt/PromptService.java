//package main.java.com.llm.llmReativa.prompt;

package com.llm.llmReativa.prompt;

import java.util.List;
import org.springframework.stereotype.Service;

@Service 
public class PromptService {

   
    public String gerarPrompt(List<String> topics, List<Double> stocks) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are a financial analyst AI. Analyze the following stock market data and provide insights:\n\n");
        
        
        if (topics != null && !topics.isEmpty()) {
            prompt.append("Recent News Topics:\n");
            for (int i = 0; i < topics.size(); i++) {
                prompt.append(String.format("%d. %s\n", i + 1, topics.get(i)));
            }
            prompt.append("\n");
        }
        
        
        if (stocks != null && !stocks.isEmpty()) {
            prompt.append("Stock Prices (last 7 days):\n");
            for (int i = 0; i < stocks.size(); i++) {
                prompt.append(String.format("Stock %d: $%.2f\n", i + 1, stocks.get(i)));
            }
            prompt.append("\n");
        }
        
        prompt.append("Please provide:\n");
        prompt.append("1. Market sentiment analysis\n");
        prompt.append("2. Key trends and patterns\n");
        prompt.append("3. Risk assessment\n");
        prompt.append("4. Investment recommendations\n");
        
        return prompt.toString();
    }


}