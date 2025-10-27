package com.reativaMVC1.mvc1.Aux;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.reativaMVC1.mvc1.Entity.StockEntity;

// imperative way

@Service
public class StockCollect {



   
    private String apiKey = "";

    
  

    public String buildApiUrl(String typeOfTime, String symbol, String outputsize) {
        return "https://www.alphavantage.co/query?function=" + typeOfTime + "&symbol=" + symbol + "&outputsize=" + outputsize + "&apikey=" + apiKey + "&datatype=json";
    }

    public String collectData(String symbol) {
        String url = buildApiUrl("TIME_SERIES_DAILY", symbol, "compact");
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("API request failed: " + response.body());
                return null;
            }
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

public List<StockEntity> processingData(String body, String symbol) {
        if (body == null || body.isEmpty()) {
            return Collections.emptyList();
        }

        List<StockEntity> stockEntityList = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);

        if (!jsonObject.has("Time Series (Daily)")) {
            System.err.println("Resposta da API não contém 'Time Series (Daily)': " + body);
            return Collections.emptyList();
        }

        JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");

        for (String date : timeSeries.keySet()) {
            JsonObject data = timeSeries.getAsJsonObject(date);

            // Cria e preenche o objeto StockEntity diretamente
            StockEntity entity = new StockEntity();
            entity.setName(symbol); // O nome é o símbolo da ação
            entity.setDate(date);
            entity.setOpenPrice(data.get("1. open").getAsDouble());
            entity.setHighPrice(data.get("2. high").getAsDouble());
            entity.setClosePrice(data.get("4. close").getAsDouble());
            entity.setNews_id(1); // Valor fixo, como solicitado

            stockEntityList.add(entity);
        }

        return stockEntityList;
    }

    
}
