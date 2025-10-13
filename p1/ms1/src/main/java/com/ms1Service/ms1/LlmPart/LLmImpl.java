package com.ms1Service.ms1.LlmPart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LLmImpl implements llmService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public LLmImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getAnalysis() {
        
        try{

            String url = "http://mvc1IA/graphql"; // modificar aqui
            String query = "query { getAnalysis }";
            String jsonPayload = String.format("{\"query\": \"%s\"}", query);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if(response.getStatusCode().is2xxSuccessful()){
                JsonNode root = objectMapper.readTree(response.getBody());
                String resultMessage = root.path("data").path("getAnalysis").asText();
                System.out.println("Response from llm-ms: " + resultMessage);
                return resultMessage;
            }
        
        }catch (Exception e){
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }


        return "Análise do LLM";
    }

    @Override
    public String getSummary() {
        // Lógica para obter o resumo do LLM nop
        return "Resumo do LLM";
    }
    
}