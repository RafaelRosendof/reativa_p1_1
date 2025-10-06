package com.DB.db.Redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.DB.db.Entity.Stocks;

@Service
public class RedisImperativeService {
    
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisImperativeService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // save method with ttl 
    public Boolean set(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl.toMillis(), TimeUnit.MILLISECONDS);
        return true;
    }

    // search 
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Stocks getDataSymbol(String stockSymbol) {
        String key = "stock:data:" + stockSymbol;
        String data = get(key);
        
        if (data == null) {
            return null;
        }
        
        String[] parts = data.split(",");
        Stocks stock = new Stocks();
        stock.setId(Integer.parseInt(parts[0]));
        stock.setName(parts[1]);
        stock.setOpenPrice(Double.parseDouble(parts[2]));
        stock.setClosePrice(Double.parseDouble(parts[3]));
        stock.setHighPrice(Double.parseDouble(parts[4]));
        stock.setDate(parts[5]);
        // later we gonna see about the news_id
        
        return stock;
    }

    // increment counter 
    public Long incrementCounter(String stockSymbol) {
        String key = "stock:requests:" + stockSymbol;
        return redisTemplate.opsForValue().increment(key);
    }

    public Long getRequestCount(String stockSymbol) {
        String key = "stock:requests:" + stockSymbol;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value) : 0L;
    }

    // filter and get the top most requested stock
    public Stocks getTopStock() {
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

        return getDataSymbol(topStockSymbol);
    }

    public Boolean cacheStockData(String stockSymbol, String data) {
        String key = "stock:data:" + stockSymbol;
        Duration ttl = Duration.ofHours(1); 
        return set(key, data, ttl);
    }

    public String getCachedStockData(String stockSymbol) {
        String key = "stock:cache:" + stockSymbol;
        return get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key) != null && redisTemplate.delete(key) > 0;
    }

    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public String getTopRequestedStocks(int limit) {
        Set<String> keys = redisTemplate.keys("stock:requests:*");
        
        if (keys == null || keys.isEmpty()) {
            return "";
        }
        
        List<StockRequest> requests = keys.stream()
            .map(key -> {
                String symbol = key.replace("stock:requests:", "");
                String countStr = redisTemplate.opsForValue().get(key);
                long count = countStr != null ? Long.parseLong(countStr) : 0L;
                return new StockRequest(symbol, count);
            })
            .sorted((a, b) -> Long.compare(b.count, a.count))
            .limit(limit)
            .collect(Collectors.toList());
        
        return requests.stream()
            .map(sr -> sr.symbol + ":" + sr.count)
            .collect(Collectors.joining(","));
    }

    public Long resetDailyCounters() {
        Set<String> keys = redisTemplate.keys("stock:requests:*");
        
        if (keys == null || keys.isEmpty()) {
            return 0L;
        }
        
        return redisTemplate.delete(keys);
    }

    // TODO -> later we gonna do a snapshot of the data in the redis and save in the postgres 
    // TODO -> after the snapshot clear the redis and save the new data

    public List<Stocks> getAllCachedStocks() {
        Set<String> keys = redisTemplate.keys("stock:data:*");
        
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        
        return keys.stream()
            .map(key -> {
                String stockSymbol = key.replace("stock:data:", "");
                return getDataSymbol(stockSymbol);
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
