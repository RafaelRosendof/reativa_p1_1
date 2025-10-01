package com.DB.db.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DB.db.Entity.News;
import com.DB.db.DAO.NewsInterface;
import com.DB.db.DAO.StockInterface;


import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;


@Service
public class NewsService {

    public NewsInterface newsInterface;
    public StockInterface stockInterface;

    @Autowired
    public NewsService(NewsInterface newsInterface , StockInterface stockInterface) {
        this.newsInterface = newsInterface;
        this.stockInterface = stockInterface;
    }
    
    public Mono<News> findById(int id) {
        return newsInterface.findById(id);
    }

    public Mono<News> addNews(News news) {
        return newsInterface.save(news);
    }

    public void deleteNews(int id) {
        newsInterface.deleteById(id).subscribe();
    }

    public Mono<News> patchNews(int id, News newNews) {
        return newsInterface.findById(id).flatMap(news -> {
            if (newNews.getFont() != null) {
                news.setFont(newNews.getFont());
            }
            if (newNews.getTitle() != null) {
                news.setTitle(newNews.getTitle());
            }
            if (newNews.getDescription() != null) {
                news.setDescription(newNews.getDescription());
            }
            if (newNews.getUrl() != null) {
                news.setUrl(newNews.getUrl());
            }
            if (newNews.getStockId() != 0) {
                news.setStockId(newNews.getStockId());
            }
            return newsInterface.save(news);
        });
    }



    public Mono<News> getNewsByTitle(String title) {
        return newsInterface.findByTitle(title);
    }



    public Flux<News> getAllNews() {
        return newsInterface.findAll();
    }

    public Flux<News> getAllNewsByStockId(int stockId) {
        return newsInterface.findAllByStockId(stockId);
    }

    public Flux<News> getAllNewsByStockName(String stockName) {
        return stockInterface.findAllByName(stockName)
                .flatMap(stock -> newsInterface.findAllByStockId(stock.getId()));
    }


    public Flux<News> getAllNewsByFont(String font) {
        return newsInterface.findAllByFont(font);
    }

    // Basic methods is done, now going to be for scraping or api using
}