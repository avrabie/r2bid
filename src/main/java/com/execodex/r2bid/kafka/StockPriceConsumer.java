package com.execodex.r2bid.kafka;

import com.execodex.r2bid.model.StockPrice;
import com.execodex.r2bid.repository.StockRepository;
import com.execodex.r2bid.sinks.MyStringSink;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockPriceConsumer {

    private final MyStringSink myStringSink;
    private final StockRepository stockRepository;

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
//        StockPrice stockPrice = objectMapper.convertValue(message, StockPrice.class);
        // use the converter to convert StockPrice to Stock

        // save the stock to the database
//        Mono<Stock> save = stockRepository.save(stock);
//        save
//                .doOnSuccess(s -> log.info("Stock saved to database: {}", s))
//                .subscribe();

        // TODO implement further logic to update the database or push to SSE
    }
}
