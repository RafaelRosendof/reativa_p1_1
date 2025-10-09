package com.reativaMVC1.mvc1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reativaMVC1.mvc1.Aux.NewsCollector;
import com.reativaMVC1.mvc1.DAO.NewsDAO;
import com.reativaMVC1.mvc1.Entity.NewsEntity;
import com.reativaMVC1.mvc1.Redis.RedisImperativeService;

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
    private final RedisImperativeService redisService;
    private final NewsCollector newsCollector = new NewsCollector();

    @Autowired
    public NewsService(NewsDAO newsDAO, NewsEntity newsEntity, RedisImperativeService redisService) {
        this.newsDAO = newsDAO;
        this.newsEntity = newsEntity;
        this.redisService = redisService;
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

    public String createNews(String stock){

        // função que vai retornar a notícia já pronta tudo feito ja  
        return newsCollector.processNewsData(stock);
    }

    public String getNewsTop1(){

        String top1 = redisService.getTop2Request().get(0);

        String news1 = createNews(top1);

        System.out.println("News 1: " + news1);
        
        return news1;

    }

    public String getNewsTop2(){

        String top2 = redisService.getTop2Request().get(1);

        String news2 = createNews(top2);

        System.out.println("News 1: " + news2);
        
        return news2;
    
    }

    public String giveBackPrompt(){
        
        return "This is a prompt";
    }

}