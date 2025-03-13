package com.execodex.r2bid.kafka;

import com.execodex.r2bid.converter.StockPriceMapper;
import com.execodex.r2bid.entity.Stock;
import com.execodex.r2bid.model.StockPrice;
import com.execodex.r2bid.model.StockPriceResponse;
import com.execodex.r2bid.repository.StockRepository;
import com.execodex.r2bid.sinks.MyStringSink;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class StockPriceListener {

    private final MyStringSink myStringSink;
    private final StockRepository stockRepository;
    private final StockPriceMapper stockPriceMapper;
    ObjectMapper objectMapper = new ObjectMapper();

    public StockPriceListener(MyStringSink myStringSink, StockRepository stockRepository, StockPriceMapper stockPriceMapper) {
        this.myStringSink = myStringSink;
        this.stockRepository = stockRepository;

        this.stockPriceMapper = stockPriceMapper;
    }


    @KafkaListener(topics = "stock-prices", groupId = "r2bid-group")
    public void consumeStockPrice(String message) {
        //investigate why this is not working
//        myStringSink.next("From Kakfa stock-prices :: "+message);
        log.info("KafkaListener Received stock price: {}", message);

        StockPriceResponse stockPriceResponse;
        try {
            stockPriceResponse = objectMapper.readValue(message, StockPriceResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // use the converter to convert StockPrice to Stock
        StockPrice stockPrice;
        Optional<String> first = stockPriceResponse.getTimeSeries().keySet().stream().findFirst();
        if (first.isPresent()) {
            stockPrice = stockPriceResponse.getTimeSeries().get(first.get());
        } else {
            throw new RuntimeException("No stock price found in response");
        }
        Stock stock = stockPriceMapper.stockPriceToStock(stockPrice);
        stock.setSymbol(stockPriceResponse.getMetaData().getSymbol());
        // give a string like this 2025-03-07 19:59:00 parse to LocalDateTime

        stock.setTimestamp(LocalDateTime.now());

        // save the stock to the database
        Mono<Stock> save = stockRepository.save(stock);
        save
                .doOnSuccess(s -> log.info("Stock saved to database: {}", s))
                .subscribe();

        // TODO implement further logic to update the database or push to SSE
    }
}
