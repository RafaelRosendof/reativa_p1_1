package com.reativaMVC1.mvc1.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reativaMVC1.mvc1.Entity.NewsEntity;


public interface NewsDAO extends JpaRepository<NewsEntity, Integer> {

    List<NewsEntity> findAllByStock_id(int stock_id);
    NewsEntity findByTitle(String title);
    NewsEntity findByStock_id(int stock_id);

    NewsEntity findByUrl(String url);


}