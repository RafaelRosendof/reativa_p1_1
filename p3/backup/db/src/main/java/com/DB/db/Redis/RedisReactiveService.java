package com.DB.db.Redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.DB.db.Entity.Stocks;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
public class RedisReactiveService {
    
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisReactiveService(ReactiveRedisTemplate<String, String> redisTemplate ) {
        this.redisTemplate = redisTemplate;
    }


    //save method with ttl 
    public Mono<Boolean> set(String key , String value , Duration ttl){
        return redisTemplate.opsForValue()
        .set(key, value, ttl);
    }

    // search 
    public Mono<String> get(String key){
        return redisTemplate.opsForValue()
        .get(key);
    }

    public Mono<Stocks> getDataSymbol(String stockSymbol) {
        String key = "stock:data:" + stockSymbol;
        return get(key)
            .map(data -> {
                if (data == null) {
                    return null;
                }
                String[] parts = data.split(",");
                Stocks stock = new Stocks();
                stock.setId(Integer.parseInt(parts[0])); // if id is Integer
                stock.setName(parts[1]);
                stock.setOpenPrice(Double.parseDouble(parts[2]));
                stock.setClosePrice(Double.parseDouble(parts[3]));
                stock.setHighPrice(Double.parseDouble(parts[4]));
                stock.setDate(parts[5]);
                // latter we gonna see about the news_id// if newsId is Integer
                return stock;
            });
    }

        
       
    
    // increment counter 
    public Mono<Long> incrementCounter(String stockSymbol){
        String key = "stock:requests:" + stockSymbol;
        return redisTemplate.opsForValue().increment(key);
   }

    public Mono<Long> getRequestCount(String stockSymbol){
        String key = "stock:requests:" + stockSymbol;
        return redisTemplate.opsForValue()
                .get(key)
                .map(Long::parseLong)
                .defaultIfEmpty(0L);
   }


   //filter and get the top 2 n most requested stocks
    public Mono<Stocks> getTopStock(){
        List<String> topStocks = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        topStocks.add("AAPL");
        topStocks.add("MSFT");
        topStocks.add("GOOGL");
        topStocks.add("AMZN");
        topStocks.add("TSLA");
        topStocks.add("META");
        
        for(int i = 0 ; i < topStocks.size() ; i++){
            String stock = topStocks.get(i);
            Long count = getRequestCount(stock).block();
            counts.add(count);
        }

        long maxCount = counts.stream().mapToLong(v -> v).max().orElse(0L);
        
        int index = counts.indexOf(maxCount);
        String topStockSymbol = topStocks.get(index);

        return getDataSymbol(topStockSymbol);
        
    }

    public Mono<Boolean> cacheStockData(String stockSymbol , String data){
        String key = "stock:data:" + stockSymbol;
        Duration ttl = Duration.ofHours(1); 
        return set(key, data, ttl);
   }

    public Mono<String> getCachedStockData(String stockSymbol) {
        String key = "stock:cache:" + stockSymbol;
        return get(key);
    }


    public Mono<Boolean> delete(String key) {
        return redisTemplate.delete(key).map(count -> count > 0);
    }

 
    public Mono<Boolean> exists(String key) {
        return redisTemplate.hasKey(key);
    }



    public Mono<String> getTopRequestedStocks(int limit) {
        return redisTemplate.keys("stock:requests:*")
            .flatMap(key -> 
                redisTemplate.opsForValue().get(key)
                    .map(count -> new StockRequest(
                        key.replace("stock:requests:", ""), 
                        Long.parseLong(count)
                    ))
            )
            .sort((a, b) -> Long.compare(b.count, a.count))
            .take(limit)
            .map(sr -> sr.symbol + ":" + sr.count)
            .reduce((a, b) -> a + "," + b)
            .defaultIfEmpty("");
    }

    public Mono<Long> resetDailyCounters() {
        return redisTemplate.keys("stock:requests:*")
            .flatMap(redisTemplate::delete)
            .reduce(0L, Long::sum);
    }

    //TODO -> latter we gonna do a snapshot of the data in the redis and save in the postgres 
    //TODO -> after the snapshot clear de redis and save the new data

    public Flux<Stocks> getAllCachedStocks(){

        return redisTemplate.keys("stock:data:*")
            .flatMap(key -> {
                String stockSymbol = key.replace("stock:data:", "");
                return getDataSymbol(stockSymbol);
            });
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
