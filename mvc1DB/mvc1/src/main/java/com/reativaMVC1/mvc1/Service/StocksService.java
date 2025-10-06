package com.reativaMVC1.mvc1.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reativaMVC1.mvc1.DAO.StockDAO;
import com.reativaMVC1.mvc1.Entity.StockEntity;

@Service
public class StocksService {

    private final StockDAO stockDAO;
    private final StockEntity stockEntity;

    @Autowired
    public StocksService(StockDAO stockDAO, StockEntity stockEntity) {
        this.stockDAO = stockDAO;
        this.stockEntity = stockEntity;
    }

    public StockEntity createStock(StockEntity stock) {
        return stockDAO.save(stock);
    }
    public StockEntity getStockById(int id) {
        return stockDAO.findById(id).orElse(null);
    }
    // latter we get the real data from the API
    public StockEntity updateStock(int id, StockEntity updatedStock) {
        return stockDAO.findById(id).map(stock -> {
            stock.setName(updatedStock.getName());
            stock.setDate(updatedStock.getDate());
            stock.setOpenPrice(updatedStock.getOpenPrice());
            stock.setClosePrice(updatedStock.getClosePrice());
            stock.setHighPrice(updatedStock.getHighPrice());
            //stock.setLowPrice(updatedStock.getLowPrice());
            stock.setNews_id(updatedStock.getNews_id());
            return stockDAO.save(stock);
        }).orElse(null);
    }
    public void deleteStock(int id) {
        stockDAO.deleteById(id);
    }

    public List<StockEntity> getAllStocks() {
        return stockDAO.findAll();
    }

    public StockEntity getStockByName(String name) {
        return stockDAO.findByName(name);
    }

    public List<StockEntity> getStocksByDate(String date) {
        return stockDAO.findAllByDate(date);
    }

    // latter for the methods of the redis 


}