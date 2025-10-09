package com.reativaMVC1.mvc1.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reativaMVC1.mvc1.Aux.StockCollect;
import com.reativaMVC1.mvc1.DAO.StockDAO;
import com.reativaMVC1.mvc1.Entity.StockEntity;
import com.reativaMVC1.mvc1.Redis.RedisImperativeService;

@Service
public class StocksService {

    private final StockDAO stockDAO;
    private final StockEntity stockEntity;
    private final RedisImperativeService redisService;
    private final StockCollect stockCollect;
    private final NewsService newsService;

    @Autowired
    public StocksService(StockDAO stockDAO, StockEntity stockEntity, RedisImperativeService redisService, NewsService newsService , StockCollect stockCollect) {
        this.stockDAO = stockDAO;
        this.stockEntity = stockEntity;
        this.redisService = redisService;
        this.newsService = newsService;
        this.stockCollect = stockCollect;
    }

    public StockEntity findById(int id) {
        return stockDAO.findById(id).orElse(null);
    }

    public List<StockEntity> findAll() {
        return stockDAO.findAll();
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


    public StockEntity createAllStock(String stockName){
        /*
         * 1. collect the data from the API, the API is gonna return a lot of json one for each day
         * 2. the parser is already done in the StockCollect class
         * 3. get the data here and in a for loop create a StockEntity for each day and save it in the database
         * 4. return the list of StockEntity
         */
        //return stockDAO.save(stockEntity); // for now
        // the collectData need to be in StockEntity format 
        List<StockEntity> stocks = stockCollect.collectData(stockName);
    }

    public List<StockEntity> giveBackStock1(){
        /*
         * 1. get the top 2 stocks from the redis
         * 2. get all the data from the database based on a stockName and the stockname is the top of the redis function getTop2Request
         */
        return stockDAO.findAll();
    }

    public List<StockEntity> giveBackStock2 (){
        /*
         * 1. get the top 2 stocks from the redis
         * 2. get all the data from the database based on a stockName and the stockname is the top of the redis function getTop2Request
         */
        return stockDAO.findAll();
    }

    public String giveBackPrompt(){
        /*
         * Combine the news + data from the stocks and create a prompt to send back to ms1
         */
        return "This is a prompt";
    }

}