package com.DB.db.DAO;

import java.util.List;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.DB.db.Entity.News;
import com.DB.db.Entity.Stocks;

@Repository
public interface NewsInterface extends ReactiveCrudRepository<News, Integer> {

    /*
     * basic crud
     */
    
    Mono<News> findByStockId(int id);

    Mono<News> findByTitle(String title);

    Flux<News> findAllByStockId(int stockId);

    Flux<News> findAllByFont(String font);

    // more latter


    
}