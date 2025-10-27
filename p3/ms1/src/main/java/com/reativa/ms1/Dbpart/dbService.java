package com.reativa.ms1.Dbpart;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public interface dbService {

    public Mono<String> saveRedisData(@Argument String symbol);

    public Mono<String> getTop1Stock();

    public Mono<String> getTop2Stocks();

    public Mono<String> stopRedis();
    
}