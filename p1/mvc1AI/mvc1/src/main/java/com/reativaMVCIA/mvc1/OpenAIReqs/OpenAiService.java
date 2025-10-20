package com.reativaMVCIA.mvc1.OpenAIReqs;


import java.net.http.HttpClient;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reativaMVCIA.mvc1.prompt.PromptService;

@Service
public class OpenAiService implements ChatService  {
    
    private final PromptService promptService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private final String api_key = "";

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    // REVIEW THIS

    @Autowired
    public OpenAiService(RestTemplate restTemplate, PromptService promptService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.promptService = promptService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String sendGraphQLToDb(){
        try{

            String url_graphql = "http://database-ms/graphql";
            String graphQlQuery = "{ \"query\": \"query { giveBackToAIPrompt1 }\" }";

            System.out.println("\n\n Sending GraphQL request to database-ms: " + graphQlQuery + "\n\n");

            HttpHeaders headersGraphql = new HttpHeaders();
            headersGraphql.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(graphQlQuery, headersGraphql);
            ResponseEntity<String> response = restTemplate.postForEntity(url_graphql, request, String.class);


            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String promptQL = root.path("data").path("giveBackToAIPrompt1").asText();
                System.out.println("Prompt from GraphQL: " + promptQL);

                return "Prompt from GraphQL: " + promptQL;
            }


        } catch(Exception e){
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
        
        return null;
    
    }

    @Override
    public String getAnalysis() {

        String data = getPrompt();

        System.out.println("\n\n Data received in getAnalysis method: " + data + "\n\n");

        String response = sendChatWithPrompt(data,  "gpt-4o-mini" , api_key);

        return response;


    }
    

    @Override
    public String getPrompt() {
        String data = sendGraphQLToDb();
        return promptService.gerarPrompt(data);
    }

    // aqui não precisa
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
        
        //String prompt = getPrompt();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        String requestBody = String.format(
            "{ \"model\": \"%s\", \"messages\": [ { \"role\": \"user\", \"content\": \"%s\" } ] }",
            model, prompt.replace("\"", "\\\"")
        );

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to send chat: " + response.getStatusCode());
        }
    }

}
