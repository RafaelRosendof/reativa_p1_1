//package main.java.com.llm.llmReativa.openAIrequest;

package com.llm.llmReativa.openAIrequest;

import java.util.List;
import java.util.Map;

import com.llm.llmReativa.prompt.PromptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;



@Service 
public class OpenAiChatService implements ChatService {

    private final RestTemplate restTemplate;
    private final PromptService promptService;
    private final ObjectMapper objectMapper;

    //private static final String OPENAI_API_URL = "https://api.openai.com/v1/responses";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";


    @Autowired
    public OpenAiChatService(RestTemplate restTemplate, PromptService promptService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.promptService = promptService;
        this.objectMapper = objectMapper;
    }


    @Override
    public String getPrompt(List<String> topics, List<Double> stocks) {
        return promptService.gerarPrompt(topics, stocks);
    }

    @Override
    public String getPromptQuestion(String chatQuestion) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are my system bot, please answer the following question: \n\n");
        prompt.append(chatQuestion);
        prompt.append("\n\n Please be concise and to the point.");

        return prompt.toString();
    }
    
    @Override
    public String sendChatWithPrompt(String prompt , String apiKey , String model){

        try{

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");

            Map<String , Object> requestBody = Map.of(

                "model" , model != null ? model : "@spring.ai.openai.model",
                "messages" , List.of( 
                    Map.of("role" , "user" , "content" , prompt)
                ),
                //"max_tokens", 1000,
                "max_completion_tokens", 1000
            );

            HttpEntity<Map<String , Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> resp = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
            );

            JsonNode jResp = objectMapper.readTree(resp.getBody());

            return jResp.get("choices").get(0).get("message").get("content").asText();


        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Failed to communicate with OpenAI: " + e.getMessage());

        }
    }


}