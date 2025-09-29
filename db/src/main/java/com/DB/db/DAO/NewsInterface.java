package com.DB.db.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.DB.db.Entity.News;
import com.DB.db.Entity.Stocks;

public interface NewsInterface extends JpaRepository<News , Integer> {

    /*
     * basic crud
     */
    
    News findByStockId(int stockId);
    List<News> findAll();
    List<News> findByTitle(String title);
    List<News> findByStock(Stocks stock);
}