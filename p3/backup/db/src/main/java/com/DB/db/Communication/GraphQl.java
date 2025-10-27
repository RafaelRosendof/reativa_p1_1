package com.DB.db.Communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.DB.db.Entity.Stocks;
import com.DB.db.Aux.StockCollect;
import com.DB.db.Redis.RedisReactiveService;
import com.DB.db.Service.StocksService;

@Controller
public class GraphQl {
    
    private final StocksService stocksService;
    private final RedisReactiveService redisService;
    private final StockCollect stockCollect;
    
    @Autowired
    public GraphQl(StocksService stocksService, RedisReactiveService redisService, StockCollect stockCollect) {
        this.stocksService = stocksService;
        this.redisService = redisService;
        this.stockCollect = stockCollect;
    }


    @QueryMapping
    public Mono<Stocks> getStock(@Argument int id) {
        return stocksService.findById(id);
    }

    @QueryMapping
    public Mono<Stocks> getStockByNameAndData(@Argument String name , @Argument String date){
        return redisService.incrementCounter(name)
        .then(stocksService.getStockByNameAndDate(name, date));
    }

    @QueryMapping
    public Flux<Stocks> getAllStocks(){
        return stocksService.getAllStocks();
    }

    @QueryMapping
    public Mono<Stocks> getTopStocks(@Argument int limit){
        return redisService.getTopStock();
    }

    @QueryMapping
    public void putDataInToPostgres(){
        Flux<Stocks> stocksList = stocksService.getStocksFromRedis();
        stocksService.saveStocksToPostgres(stocksList);

        System.out.println("Data transfer to Postgres initiated.");
    }


    @MutationMapping
    public Mono<Stocks> createStock(@Argument StockInput stockInput){
        Stocks stock = new Stocks(
            stockInput.getId(),
            stockInput.getName(),
            stockInput.getOpenPrice(),
            stockInput.getClosePrice(),
            stockInput.getHighPrice(),
            stockInput.getDate(),
            stockInput.getNewsId()
        );
        return stocksService.addStock(stock);
    }

    @MutationMapping
    public Mono<Stocks> updateStocks(
        @Argument Integer id,
        @Argument StockInput stockInput
    ){

        Stocks newStock = new Stocks(
            stockInput.getId(),
            stockInput.getName(),
            stockInput.getOpenPrice(),
            stockInput.getClosePrice(),
            stockInput.getHighPrice(),
            stockInput.getDate(),
            stockInput.getNewsId()
        );

        newStock.setId(id);

        return stocksService.patchStock(id, newStock);


    }


    public static class StockInput {
        private int id;
        private String name;
        private Double openPrice;
        private Double closePrice;
        private Double highPrice;
        private String date;
        private int newsId;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public void setNewsId(int newsId) { this.newsId = newsId; }

        public int getNewsId() { return newsId; }

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public Double getOpenPrice() { return openPrice; }
        public void setOpenPrice(Double openPrice) { this.openPrice = openPrice; }
        
        public Double getClosePrice() { return closePrice; }
        public void setClosePrice(Double closePrice) { this.closePrice = closePrice; }
        
        public Double getHighPrice() { return highPrice; }
        public void setHighPrice(Double highPrice) { this.highPrice = highPrice; }
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }
}