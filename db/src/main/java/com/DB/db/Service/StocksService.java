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

/*
package com.DB.db.Service;

import com.DB.db.DAO.StockReactiveRepository;
import com.DB.db.Entity.StocksReactive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StockReactiveService {

    private final StockReactiveRepository stockRepository;

    @Autowired
    public StockReactiveService(StockReactiveRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    
    public Mono<StocksReactive> findById(Integer id) {
        return stockRepository.findById(id);
    }

    
    public Mono<StocksReactive> saveStock(StocksReactive stock) {
        return stockRepository.save(stock);
    }

    
    public Mono<Boolean> deleteStock(Integer id) {
        return stockRepository.findById(id)
            .flatMap(stock -> stockRepository.delete(stock).thenReturn(true))
            .defaultIfEmpty(false);
    }

    
    public Mono<StocksReactive> updateStock(Integer id, StocksReactive newStock) {
        return stockRepository.findById(id)
            .flatMap(existingStock -> {
                existingStock.setName(newStock.getName());
                existingStock.setOpenPrice(newStock.getOpenPrice());
                existingStock.setClosePrice(newStock.getClosePrice());
                existingStock.setHighPrice(newStock.getHighPrice());
                existingStock.setDate(newStock.getDate());
                return stockRepository.save(existingStock);
            });
    }

    
    public Mono<StocksReactive> getStockByNameAndDate(String name, String date) {
        return stockRepository.findByNameAndDate(name, date);
    }

    
    public Mono<List<Double>> getStockValuesByName(String name, String date) {
        return getStockByNameAndDate(name, date)
            .map(stock -> List.of(
                stock.getOpenPrice(),
                stock.getClosePrice(),
                stock.getHighPrice()
            ))
            .defaultIfEmpty(List.of());
    }

    
    public Flux<StocksReactive> getAllStocks() {
        return stockRepository.findAll();
    }

    
    public Flux<StocksReactive> getTopStocksByClosePrice(int limit) {
        return stockRepository.findAll()
            .sort((s1, s2) -> Double.compare(s2.getClosePrice(), s1.getClosePrice()))
            .take(limit);
    }

    
    public Flux<StocksReactive> getStocksByDate(String date) {
        return stockRepository.findByDate(date);
    }
}

 */