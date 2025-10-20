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
    //private final StockEntity stockEntity;
    private final RedisImperativeService redisService;
    private final StockCollect stockCollect;
    private final NewsService newsService;

    @Autowired
    public StocksService(StockDAO stockDAO,  RedisImperativeService redisService, NewsService newsService , StockCollect stockCollect) {
        this.stockDAO = stockDAO;
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


    public String createAllStock(String stockName){
        /*
         * 1. collect the data from the API, the API is gonna return a lot of json one for each day
         * 2. the parser is already done in the StockCollect class
         * 3. get the data here and in a for loop create a StockEntity for each day and save it in the database
         * 4. return the list of StockEntity
         */
        
        String body = stockCollect.collectData(stockName);
        List<StockEntity> stocks = stockCollect.processingData(body, stockName);

        System.out.println("Collected " + stocks.size() + " stock entries for " + stockName);
        for (int i = 0 ; i < stocks.size() ; i++){
            System.out.println("Saving stock entry: " + stocks.get(i).getDate() + " for " + stocks.get(i).getName());
            stockDAO.save(stocks.get(i));
        }
        System.out.println("Stocks saved to the database.");

        if (stocks.isEmpty()) {
            return "No stock data found for symbol: " + stockName;
        } else {
            return "Stock data for " + stockName + " has been collected and saved. Total entries: " + stocks.size();
        }

    }

    public String stopRedis(){

        System.out.println("Is been here in the stopRedis method of StocksService");

        List<String> topStocks = redisService.getTop2Request();
        
        if(topStocks == null || topStocks.size() < 2){
            return "Not enough stock requests recorded in Redis.";
        }
        
        System.out.println("Top requested stocks: " + topStocks);

        for (String stockName : topStocks) {
            createAllStock(stockName);
        }

        System.out.println("Data collection and storage completed for top requested stocks.");

        redisService.clearRedis();

        return "Data collection and storage completed for top requested stocks.";
    }

    public List<StockEntity> giveBackStock1(){
        /*
         * 1. get the top 2 stocks from the redis
         * 2. get all the data from the database based on a stockName and the stockname is the top of the redis function getTop2Request
         */

        String stock1 = redisService.getTop2Request().get(0);
        System.out.println("Top requested stock 1: " + stock1);

        return stockDAO.findAllByName(stock1);
    }

    public List<StockEntity> giveBackStock2 (){
        /*
         * 1. get the top 2 stocks from the redis
         * 2. get all the data from the database based on a stockName and the stockname is the top of the redis function getTop2Request
         */
        String stock2 = redisService.getTop2Request().get(1);
        System.out.println("Top requested stock 2: " + stock2);

        return stockDAO.findAllByName(stock2);
    }

    public String giveBackPrompt1(){
        List<StockEntity> stockList1 = giveBackStock1();
        
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Stock Data for ").append(stockList1).append(":\n\n\n");
        
        String noticias1 = newsService.getNewsTop1();

        promptBuilder.append("News Data:\n").append(noticias1).append("\n\n");

        return promptBuilder.toString();

    }

    public String giveBackPrompt2(){
        List<StockEntity> stockList2 = giveBackStock2();
        
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Stock Data for ").append(stockList2).append(":\n\n\n");

        String noticias2 = newsService.getNewsTop2();

        promptBuilder.append("News Data:\n").append(noticias2).append("\n\n");

        return promptBuilder.toString();
        
    }
}