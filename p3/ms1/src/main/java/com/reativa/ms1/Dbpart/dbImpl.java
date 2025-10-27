package com.reativa.ms1.Dbpart;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class dbImpl implements dbService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public dbImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Mono<String> saveRedisData(@Argument String symbol) {
        try{
            String url = "http://MVC1/graphql"; // modificar aqui 
            String query = String.format("query { requestStocking(name: \\\"%s\\\") }", symbol);
            String jsonPayload = String.format("{\"query\": \"%s\"}", query);

            HttpHeaders headers = new HttpHeaders();


            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            System.out.println("Request sent to database-ms for symbol: " + symbol);

            if(response.getStatusCode().is2xxSuccessful()){
                JsonNode root = objectMapper.readTree(response.getBody());
                String resultMessage = root.path("data").path("requestStocking").asText();
                System.out.println("Response from database-ms: " + resultMessage);
                return Mono.just(resultMessage);
            }

        }catch(Exception e){
            e.printStackTrace();
            return Mono.just("Error: " + e.getMessage());
        }

        return Mono.just("Error: Unsuccessful response");
    }

    @Override
    public Mono<String> getTop1Stock() {
        try{
            String url = "http://MVC1/graphql";
            String query = "query { getTop1Stock}";
            String jsonPayload = String.format("{\"query\": \"%s\"}", query);

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                JsonNode root = objectMapper.readTree(response.getBody());
                String top1Stock = root.path("data").path("getTop1Stock").asText();
                System.out.println("Top 1 Stock from database-ms: " + top1Stock);
                return Mono.just("Top 1 Stock: " + top1Stock);
            
            }

        }catch (Exception e){
            e.printStackTrace();
            return Mono.just("Error: " + e.getMessage());
        }

        return Mono.just("\n\n ERROR: Não foi possível conectar ao database-ms \n\n");
    }

    @Override
    public Mono<String> getTop2Stocks() {
        try{
            String url = "http://MVC1/graphql";
            String query = "query { getTop2Stock}";
            String jsonPayload = String.format("{\"query\": \"%s\"}", query);

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                JsonNode root = objectMapper.readTree(response.getBody());
                String top2Stock = root.path("data").path("getTop2Stock").asText();
                System.out.println("Top 2 Stock from database-ms: " + top2Stock);
                return Mono.just("Top 2 Stock: " + top2Stock);
            }

        }catch (Exception e){
            e.printStackTrace();
            return Mono.just("Error: " + e.getMessage());
        }

        return Mono.just("\n\n ERROR: Não foi possível conectar ao database-ms \n\n");
    }

    @Override
    public Mono<String> stopRedis() {
        try{
            String url = "http://MVC1/graphql";
            String query = "query { stopRedis }";
            String jsonPayload = String.format("{\"query\": \"%s\"}", query);

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                JsonNode root = objectMapper.readTree(response.getBody());
                String stopRedis = root.path("data").path("stopRedis").asText();
                System.out.println("Request sent to database-ms to stop Redis and process data.");
                System.out.println("Stop Redis from database-ms: " + stopRedis);
                return Mono.just("Stop Redis: " + stopRedis);
            }

        }catch (Exception e){
            e.printStackTrace();
            return Mono.just("Error: " + e.getMessage());
        }

        return Mono.just("\n\n ERROR: Não foi possível conectar ao database-ms \n\n");
    }
}