package com.DB.db.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DB.db.Entity.News;
import com.DB.db.Entity.Stocks;

public interface StockInterface extends JpaRepository<Stocks , Integer> {

    

    
}