package com.DB.db.Aux;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Mono;


@Service
public class StockCollect {



    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public String buildApiUrl(String typeOfTime , String symbol , String outputsize) {
        return "https://www.alphavantage.co/query?function=" + typeOfTime + "&symbol=" + symbol + "&outputsize=" + outputsize + "&apikey=" + apiKey + "&datatype=json";
    }

    public StockCollect(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.alphavantage.co").build();
        this.objectMapper = new ObjectMapper();
    }

    // reactive calling a blocking method
    public Mono<String> collectDataMono(String function, String symbol, String outputsize) {
        return Mono.fromCallable(() -> collectData(function, symbol, outputsize));
    }

    public Mono<String> getStockData(String function , String symbol , String outputsize){

        String url = buildApiUrl(function, symbol, outputsize);

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), 
                    clientResponse -> clientResponse.bodyToMono(String.class) 
                        .flatMap(errorBody -> {
                            System.err.println("API request failed: " + errorBody);
                            return Mono.error(new RuntimeException("API request failed with status: " + clientResponse.statusCode()));
                        })
                    )
                .bodyToMono(String.class)
                .flatMap(this::processResponse)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.empty();
                });
    }

    public Mono<String> processResponse(String responseBody) {

        return Mono.fromCallable( () -> {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode timeSeriesNode = rootNode.path("Time Series (Daily)");
            
            ObjectNode resultJson = objectMapper.createObjectNode();
            ObjectNode filteredTimeSeries = objectMapper.createObjectNode();

            Iterator<String> dateKeys = timeSeriesNode.fieldNames();
            int count = 0;
            while (dateKeys.hasNext() && count < 10) {
                String date = dateKeys.next();
                filteredTimeSeries.set(date, timeSeriesNode.get(date));
                count++;
            }

            resultJson.set("Meta Data", rootNode.path("Meta Data"));
            resultJson.set("Time Series (Daily)", filteredTimeSeries);
            
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultJson);
        });
    }
    

    public String collectData(String function, String symbol, String outputsize) {

        String url = buildApiUrl(function, symbol, outputsize);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .GET()
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                // In a real app, you might throw a custom exception here
                System.err.println("API request failed: " + response.body());
                return null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode timeSeriesNode = rootNode.path("Time Series (Daily)");
            
            ObjectNode resultJson = objectMapper.createObjectNode();
            ObjectNode filteredTimeSeries = objectMapper.createObjectNode();

            Iterator<String> dateKeys = timeSeriesNode.fieldNames();
            int count = 0;
            while (dateKeys.hasNext() && count < 10) {
                String date = dateKeys.next();
                filteredTimeSeries.set(date, timeSeriesNode.get(date));
                count++;
            }

            resultJson.set("Meta Data", rootNode.path("Meta Data"));
            resultJson.set("Time Series (Daily)", filteredTimeSeries);
            
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultJson);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
