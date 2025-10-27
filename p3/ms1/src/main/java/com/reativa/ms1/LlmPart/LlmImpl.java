package com.reativa.ms1.LlmPart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class LlmImpl implements llmService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public LlmImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
     
    }

    @Override 
    public Mono<String> getAnalisys(){

        try{

            String url = "http://MVC1IA/graphql";
            String query = "query { getAnalisys }";
            String jsonPayload = String.format("{\"query\": \"%s\"}", query);

            System.out.println("Sending GraphQL request to MVC1IA: " + jsonPayload);
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(jsonPayload, headers);

            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                com.fasterxml.jackson.databind.JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response.getBody());
                String resultMessage = root.path("data").path("getAnalisys").asText();
                System.out.println("Response from llm-ms: " + resultMessage);
                return Mono.just(resultMessage);
            }
            return Mono.just("Error: Unsuccessful response");

        } catch (Exception e){
            e.printStackTrace();
            return Mono.just("Error: " + e.getMessage());
        }
    }

    @Override
    public Mono<String> getSummary(){

        try{
            String url = "http://MVC1IA/graphql"; 
            String query = "query { getSummary }";
            String jsonPayload = String.format("{\"query\": \"%s\"}", query);

            System.out.println("Sending GraphQL request to MVC1IA: " + jsonPayload);

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();

            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(jsonPayload, headers);

            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if(response.getStatusCode().is2xxSuccessful()){
                com.fasterxml.jackson.databind.JsonNode root = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response.getBody());
                String resultMessage = root.path("data").path("getSummary").asText();
                System.out.println("Response from llm-ms: " + resultMessage);
                return Mono.just(resultMessage);
            }
            return Mono.just("Error: Unsuccessful response");

        }catch (Exception e){
            e.printStackTrace();
            return Mono.just("Error: " + e.getMessage());
        }

    }
}

/*
        try{

            String url = "http://MVC1IA/graphql"; 
            String query = "query { isAlive }";
            String jsonPayload = String.format("{\"query\": \"%s\"}", query);

            System.out.println("Sending GraphQL request to MVC1IA: " + jsonPayload);

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if(response.getStatusCode().is2xxSuccessful()){
                JsonNode root = objectMapper.readTree(response.getBody());
                String resultMessage = root.path("data").path("isAlive").asText();
                System.out.println("Response from llm-ms: " + resultMessage);
                return resultMessage;
            }
        
        }catch (Exception e){
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }


        return "Análise do LLM";
    }
 */