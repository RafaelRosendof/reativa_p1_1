package com.reativa.part.Redis;

import org.springframework.stereotype.Service;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

@Service
public class RedisReactive{
    
    private final ReactiveRedisTemplate<String , String> redisTemplate;
    private static final String LEADERBOARD_KEY = "stock:requests:leaderboard";

    @Autowired
    public RedisReactive(ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Boolean> set(String key , String value , Duration ttl){
        return redisTemplate.opsForValue().set(key, value, ttl);
    }

    public Mono<String> recordStockRequest(String stockName){ 
        return redisTemplate.opsForZSet().incrementScore(LEADERBOARD_KEY, stockName, 1)
            .map(newScore -> "Success") 
            .onErrorResume(e -> {
                e.printStackTrace(); 
                return Mono.just("Error");
            });
    }

    public Flux<String> getTop2Request(){
        return redisTemplate.opsForZSet().reverseRange(LEADERBOARD_KEY, Range.closed(0L, 1L));
    }

    public Mono<Void> clearRedis(){
        return redisTemplate.delete(LEADERBOARD_KEY).then();
    }

}