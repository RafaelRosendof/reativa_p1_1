//package main.java.com.llm.llmReativa.Communication;
package com.llm.llmReativa.Communication;


import java.util.List;

import com.llm.llmReativa.openAIrequest.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


@Controller
public class GraphQl {
    
    private final ChatService chatService;

    @Value("${spring.ai.openai.api-key}")
    private String defaultApiKey;

    @Autowired
    public GraphQl(ChatService chatService) {
        this.chatService = chatService;

    }

    @MutationMapping
    public AnalysisResponse analyseStock(@Argument AnalysisInput input){
        try{

            String apiKey = input.getApiKey() != null ? input.getApiKey() : defaultApiKey;

            if(apiKey == null || apiKey.isEmpty()){
                return new AnalysisResponse(null, "API key is required", false);
            }

            String prompt = chatService.getPrompt(input.getTopics(), input.getStocks());
            String response = chatService.sendChatWithPrompt(prompt, apiKey, input.getModel());

            return new AnalysisResponse(response, null, true);

        }catch (Exception e){
            return new AnalysisResponse(null, "Failed to analyze stocks: " + e.getMessage(), false);
        }
    }

    @QueryMapping
    public String generatePrompt(@Argument List<String> topics, @Argument List<Double> stocks) {
        return chatService.getPrompt(topics, stocks);
    }

}
