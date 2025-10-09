package com.reativaMVC1.mvc1.Aux;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

// imperative way

@Service
public class StockCollect {



    @Value("${alphavantage.api.key}")
    private String apiKey;

    
  

    public String buildApiUrl(String typeOfTime , String symbol , String outputsize) {
        return "https://www.alphavantage.co/query?function=" + typeOfTime + "&symbol=" + symbol + "&outputsize=" + outputsize + "&apikey=" + apiKey + "&datatype=json";
    }

    public String collectData(String symbol) {

        String url = buildApiUrl("TIME_SERIES_DAILY", symbol, "compact");

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .GET()
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
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
