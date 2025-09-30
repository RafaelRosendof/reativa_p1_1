package com.DB.db.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DB.db.Entity.News;
import com.DB.db.DAO.NewsInterface;
import com.DB.db.DAO.StockInterface;

@Service
public class NewsService {

    public NewsInterface newsInterface;
    public StockInterface stockInterface;

    @Autowired
    public NewsService(NewsInterface newsInterface , StockInterface stockInterface) {
        this.newsInterface = newsInterface;
        this.stockInterface = stockInterface;
    }
    

    public News saveNews(News news) {
        return newsInterface.save(news);
    }

    public News getNewsById(int id) {
        return newsInterface.findById(id).orElse(null);
    }

    public boolean deleteNews(int id) {
        return newsInterface.findById(id).map(news -> {
            newsInterface.delete(news);
            return true;
        }).orElse(false);
    }

    public News updateNews(int id , News newNews) {
        return newsInterface.findById(id).map(news -> {
            news.setFont(newNews.getFont());
            news.setTitle(newNews.getTitle());
            news.setDescription(newNews.getDescription());
            news.setUrl(newNews.getUrl());
            news.setStock(newNews.getStock());
            return newsInterface.save(news);
        }).orElse(null);
    }

    public News getNewsByTitle(String title) {
        return newsInterface.findAll().stream()
                .filter(news -> news.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public Iterable<News> getAllNews() {
        return newsInterface.findAll();
    }


    public List<News> getNewsByStockId(int stockId) {
        return newsInterface.findAll().stream()
                .filter(news -> news.getStock() != null && news.getStock().getId() == stockId)
                .toList();
    }

    // Basic methods is done, now going to be for scraping or api using
}