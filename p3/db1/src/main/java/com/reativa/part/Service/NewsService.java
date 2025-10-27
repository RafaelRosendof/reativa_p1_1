package com.reativa.part.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import com.reativa.part.Entity.News;
import com.reativa.part.Redis.RedisReactive;
import com.reativa.part.Aux.NewsCollector;
import com.reativa.part.DAO.NewsInterface;
import com.reativa.part.DAO.StockInterface;

// corrigir o problema do redis 

@Service
public class NewsService {

    public NewsInterface newsInterface;
    public StockInterface stockInterface;
    public RedisReactive redisService;
    public NewsCollector newsCollector = new NewsCollector();

    @Autowired
    public NewsService(NewsInterface newsInterface , StockInterface stockInterface, RedisReactive redisService) {
        this.newsInterface = newsInterface;
        this.stockInterface = stockInterface;
        this.redisService = redisService;
    }
    
    public Mono<News> findById(int id) {
        return newsInterface.findById(id);
    }

    public Mono<News> addNews(News news) {
        return newsInterface.save(news);
    }


    public String createNews(String news){
        return newsCollector.processNewsData(news);
    }

    // correct this method and done 
    public Mono<String> getNewsTop1(){

        String top1 = "AMZN"; // just to ignore test propourse

        String news1 = createNews(top1);

        System.out.println("News 1: " + news1);

        return Mono.just(news1);

    }

    public Mono<String> getNewsTop2(){

        String top2 = "APPL"; // just to ignore test propourse 

        String news2 = createNews(top2);

        System.out.println("News 2: " + news2);

        return Mono.just(news2);

    }


}