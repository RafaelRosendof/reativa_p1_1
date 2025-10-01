package com.DB.db.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DB.db.DAO.NewsInterface;
import com.DB.db.DAO.StockInterface;
import com.DB.db.Entity.Stocks;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StocksService {

    public StockInterface stockInterface;
    public NewsInterface newsInterface;

    @Autowired
    public StocksService(StockInterface stockInterface , NewsInterface newsInterface) {
        this.stockInterface = stockInterface;
        this.newsInterface = newsInterface;
    }


    public Mono<Stocks> findById(int id) {
        return stockInterface.findById(id);
    }

    public Mono<Stocks> addStock(Stocks stock) {
        return stockInterface.save(stock);
    }

    public void deleteStock(int id) {
        stockInterface.deleteById(id).subscribe();
    }

    public Mono<Stocks> patchStock(int id, Stocks newStock) {
        return stockInterface.findById(id).flatMap(stock -> {
            if (newStock.getName() != null) {
                stock.setName(newStock.getName());
            }
            if (newStock.getOpenPrice() != 0) {
                stock.setOpenPrice(newStock.getOpenPrice());
            }
            if (newStock.getClosePrice() != 0) {
                stock.setClosePrice(newStock.getClosePrice());
            }
            if (newStock.getHighPrice() != 0) {
                stock.setHighPrice(newStock.getHighPrice());
            }
            if (newStock.getDate() != null) {
                stock.setDate(newStock.getDate());
            }
            return stockInterface.save(stock);
        });
    }

    public Mono<Stocks> getStockBySymbol(String symbol) {
        return stockInterface.findBySymbol(symbol);
    }

    public Flux<Stocks> getStocksByDate(String date) {
        return stockInterface.findByDate(date);
    }

    public Flux<Stocks> getAllStocks() {
        return stockInterface.findAll();
    }

    // done the basic methods, now gonne be for scraping or api using 


}
