package com.reativaMVC1.mvc1.Redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.reativaMVC1.mvc1.Entity.StockEntity;

@Service
public class RedisImperativeService {
    
    private final RedisTemplate<String , String> redisTemplate;

    @Autowired
    public RedisImperativeService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean set(String key , String value , Duration ttl) {
        redisTemplate.opsForValue().set(key , value , ttl.toMillis() , TimeUnit.MILLISECONDS);

        return true;
    }

    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public StockEntity getDataBySymbol(String symbol){
        String key = "stock:data:" + symbol;
        String data = get(key);

        if( data == null){
            return null;
        }

        String[] parts = data.split(",");
        StockEntity stock = new StockEntity();
        stock.setId(Integer.parseInt(parts[0]));
        stock.setName(parts[1]);
        stock.setDate(parts[2]);
        stock.setOpenPrice(Double.parseDouble(parts[3]));
        stock.setClosePrice(Double.parseDouble(parts[4]));
        stock.setHighPrice(Double.parseDouble(parts[5]));
        stock.setNews_id(Integer.parseInt(parts[6]));
        return stock;
    }

    public Long incrementCounter(String stockSymbol){
        String key = "stock:counter:" + stockSymbol;
        return redisTemplate.opsForValue().increment(key);
    }

    public Long getRequestCount(String stockSymbol){
        String key = "stock:counter:" + stockSymbol;
        String countStr = redisTemplate.opsForValue().get(key);
        return countStr != null ? Long.parseLong(countStr) : 0L;
    }

    public StockEntity getTopStocks(){
        List<String> topStocks = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        
        topStocks.add("AAPL");
        topStocks.add("MSFT");
        topStocks.add("GOOGL");
        topStocks.add("AMZN");
        topStocks.add("TSLA");
        topStocks.add("META");
        
        for (String stock : topStocks) {
            Long count = getRequestCount(stock);
            counts.add(count);
        }

        long maxCount = counts.stream().mapToLong(v -> v).max().orElse(0L);
        int index = counts.indexOf(maxCount);
        String topStockSymbol = topStocks.get(index);

        return getDataBySymbol(topStockSymbol);
    }

    public List<StockEntity> getTop2Stocks(){
        List<String> topStocks = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        
        topStocks.add("AAPL");
        topStocks.add("MSFT");
        topStocks.add("GOOGL");
        topStocks.add("AMZN");
        topStocks.add("TSLA");
        topStocks.add("META");
        
        for (String stock : topStocks) {
            Long count = getRequestCount(stock);
            counts.add(count);
        }

        List<StockEntity> result = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            long maxCount = counts.stream().mapToLong(v -> v).max().orElse(0L);
            int index = counts.indexOf(maxCount);
            String topStockSymbol = topStocks.get(index);
            StockEntity stockData = getDataBySymbol(topStockSymbol);
            if (stockData != null) {
                result.add(stockData);
            }
            counts.set(index, -1L); 
        }

        return result;
    }


    public Boolean cacheStockData(String stockSymbol , String data){
        String key = "stock:data:" + stockSymbol;
        Duration ttl = Duration.ofHours(1);
        return set(key , data , ttl);
    }

    public Boolean delete(String key){
        redisTemplate.delete(key);
        return true;
    }

    public String getCachedStock(String stockSymbol){
        String key = "stock:data:" + stockSymbol;
        return get(key);
    }

    public Boolean exists(String key){
        return redisTemplate.hasKey(key);
    }

    public String getTopRequestedStocks(){return null;}
    

    public Long resetDailyCounters() {
        Set<String> keys = redisTemplate.keys("stock:requests:*");
        
        if (keys == null || keys.isEmpty()) {
            return 0L;
        }
        
        return redisTemplate.delete(keys);
    }

    public List<StockEntity> getAllCachedStocks() {
        Set<String> keys = redisTemplate.keys("stock:data:*");
        
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        
        return keys.stream()
            .map(key -> {
                String stockSymbol = key.replace("stock:data:", "");
                return getDataBySymbol(stockSymbol);
            })
            .filter(stock -> stock != null)
            .collect(Collectors.toList());
    }

    private static class StockRequest {
        String symbol;
        long count;

        StockRequest(String symbol, long count) {
            this.symbol = symbol;
            this.count = count;
        }
    }

}