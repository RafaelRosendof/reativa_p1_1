package com.DB.db.Aux;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Service
public class NewsCollect {



    @Value("${alphavantage.api.key}")
    private String apiKey;

    public String buildApiUrl(String typeOfTime , String symbol , String outputsize) {
        return "https://www.alphavantage.co/query?function=" + typeOfTime + "&symbol=" + symbol + "&outputsize=" + outputsize + "&apikey=" + apiKey + "&datatype=json";
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
