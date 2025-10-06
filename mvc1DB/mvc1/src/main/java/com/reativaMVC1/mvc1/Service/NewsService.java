package com.reativaMVC1.mvc1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reativaMVC1.mvc1.DAO.NewsDAO;
import com.reativaMVC1.mvc1.Entity.NewsEntity;

import java.util.List;

/*
 * Check-list
 * Create
 * Read
 * Update
 * Delete
 * Select by date and other fields
 */
@Service
public class NewsService {
 
    private final NewsDAO newsDAO;
    private final NewsEntity newsEntity;

    @Autowired
    public NewsService(NewsDAO newsDAO, NewsEntity newsEntity) {
        this.newsDAO = newsDAO;
        this.newsEntity = newsEntity;
    }

    public NewsEntity createNews(NewsEntity news) {
        return newsDAO.save(news);
    }

    public NewsEntity getNewsById(int id) {
        return newsDAO.findById(id).orElse(null);
    }
    public NewsEntity getNewsByTitle(String title) {
        return newsDAO.findByTitle(title);
    }
    public NewsEntity getNewsByStockId(int stock_id) {
        return newsDAO.findByStock_id(stock_id);
    }
    public void deleteNews(int id) {
        newsDAO.deleteById(id);
    }
    public NewsEntity updateNews(int id, NewsEntity updatedNews) {
        return newsDAO.findById(id).map(news -> {
            news.setFont(updatedNews.getFont());
            news.setTitle(updatedNews.getTitle());
            news.setDescription(updatedNews.getDescription());
            news.setUrl(updatedNews.getUrl());
            news.setStock_id(updatedNews.getStock_id());
            return newsDAO.save(news);
        }).orElse(null);
    }

    public List<NewsEntity> getAllNews() {
        return newsDAO.findAll();
    }

    public NewsEntity getNewsByUrl(String url) {
        return newsDAO.findByUrl(url);
    }

    // Other methods for the redis service
    

}