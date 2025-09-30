package com.DB.db.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DB.db.DAO.NewsInterface;
import com.DB.db.DAO.StockInterface;
import com.DB.db.Entity.Stocks;

@Service
public class StocksService {

    public StockInterface stockInterface;
    public NewsInterface newsInterface;

    @Autowired
    public StocksService(StockInterface stockInterface , NewsInterface newsInterface) {
        this.stockInterface = stockInterface;
        this.newsInterface = newsInterface;
    }


    public Stocks findByID(int id) {
        return stockInterface.findById(id).orElse(null);
    }

    public Stocks saveStock(Stocks stock) {
        return stockInterface.save(stock);
    }

    public boolean deleteStock(int id) {
        
        return stockInterface.findById(id).map(stock -> {
            stockInterface.delete(stock);
            return true;
        }).orElse(false);

    }

    public Stocks updateStock(int id , Stocks newStock) {
        return stockInterface.findById(id).map(stock -> {
            stock.setName(newStock.getName());
            stock.setOpenPrice(newStock.getOpenPrice());
            stock.setClosePrice(newStock.getClosePrice());
            stock.setHighPrice(newStock.getHighPrice());
            return stockInterface.save(stock);
        }).orElse(null);
    }

    public Stocks getStockByName(String name , String date) {
        return stockInterface.findAll().stream()
                .filter(stock -> stock.getName().equalsIgnoreCase(name) && stock.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    public List<Double> getStocksValueByName(String name , String date){
        Stocks stock = getStockByName(name , date);
        if(stock != null){
            return List.of(stock.getOpenPrice(), stock.getClosePrice(), stock.getHighPrice());
        }
        return List.of();
    }

    public List<Stocks> getAllStocks() {
        return stockInterface.findAll();
    }

    // done the basic methods, now gonne be for scraping or api using 


}