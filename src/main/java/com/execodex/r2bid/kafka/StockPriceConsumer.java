package com.execodex.r2bid.kafka;

import com.execodex.r2bid.converter.StockPriceMapper;
import com.execodex.r2bid.entity.Stock;
import com.execodex.r2bid.model.StockPrice;
import com.execodex.r2bid.repository.StockRepository;
import com.execodex.r2bid.sinks.MyStringSink;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
public class StockPriceConsumer {

    private final MyStringSink myStringSink;
    private final StockRepository stockRepository;
    private final StockPriceMapper stockPriceMapper = StockPriceMapper.INSTANCE;
    ObjectMapper objectMapper = new ObjectMapper();

    public StockPriceConsumer(MyStringSink myStringSink, StockRepository stockRepository) {
        this.myStringSink = myStringSink;
        this.stockRepository = stockRepository;

    }


    @KafkaListener(topics = "stock-prices", groupId = "r2bid-group")
    public void consumeStockPrice(String message) {
        myStringSink.next(message);
        log.info("KafkaListener Received stock price: {}", message);
        // use objectMapper to convert message to StockPrice object
        StockPrice stockPrice;
        try {
            stockPrice = objectMapper.readValue(message, StockPrice.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // use the converter to convert StockPrice to Stock
        Stock stock = stockPriceMapper.stockPriceToStock(stockPrice);
        stock.setSymbol("TSLA");
        stock.setTimestamp(LocalDateTime.now());

        // save the stock to the database
        Mono<Stock> save = stockRepository.save(stock);
        save
                .doOnSuccess(s -> log.info("Stock saved to database: {}", s))
                .subscribe();

        // TODO implement further logic to update the database or push to SSE
    }
}
