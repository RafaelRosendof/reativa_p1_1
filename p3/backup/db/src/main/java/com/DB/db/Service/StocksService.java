package com.DB.db.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DB.db.DAO.NewsInterface;
import com.DB.db.DAO.StockInterface;
import com.DB.db.Entity.Stocks;
import com.DB.db.Redis.RedisReactiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StocksService {

    public StockInterface stockInterface;
    public RedisReactiveService redisService;
    public NewsInterface newsInterface;

    @Autowired
    public StocksService(StockInterface stockInterface , NewsInterface newsInterface, RedisReactiveService redisService) {
        this.stockInterface = stockInterface;
        this.newsInterface = newsInterface;
        this.redisService = redisService;
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

    public Mono<Stocks> getStockByNameAndDate(String name , String date) {
        return stockInterface.findByNameAndDate(name, date);
    }

    public Flux<Stocks> getStocksByDate(String date) {
        return stockInterface.findByDate(date);
    }

    public Flux<Stocks> getAllStocks() {
        return stockInterface.findAll();
    }

    // Collect all the data from the redis 
    public Flux<Stocks> getStocksFromRedis(){
        return redisService.getAllCachedStocks();
    }

    // method to save the data in to postgres 
    public void saveStocksToPostgres(Flux<Stocks> stocksList) {
        stockInterface.saveAll(stocksList).subscribe(); // the method call a stream in the argument, maybe need to change it latter 
    }

    // done the basic methods, now gonne be for scraping or api using 


}
