package com.execodex.r2bid.controller;

import com.execodex.r2bid.kafka.StockPriceProducerConfig;
import com.execodex.r2bid.service.StockPriceFetcher;
import com.execodex.r2bid.model.StockPriceResponse;
import com.execodex.r2bid.sinks.MyStringSink;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController
@RequestMapping("/api/stock")
public class StockController {


    private final StockPriceProducerConfig stockPriceProducerConfig;
    private final MyStringSink myStringSink;
    private final StockPriceFetcher stockPriceFetcher;

    public StockController(StockPriceProducerConfig stockPriceProducerConfig, MyStringSink myStringSink, StockPriceFetcher stockPriceFetcher) {
        this.stockPriceProducerConfig = stockPriceProducerConfig;
        this.myStringSink = myStringSink;
        this.stockPriceFetcher = stockPriceFetcher;
    }

    @GetMapping("/stream")
    public Flux<String> streamStockPrices() {
        return Flux.create(sink -> {
                    // Here, the producer can stream stock data to the frontend (SSE)
                    stockPriceProducerConfig.sendStockPrice("AAPL", "150.50");
                    sink.next("AAPL - 150.50");
                    // Implement continuous streaming logic to push new stock prices to the frontend
                }).delayElements(Duration.ofSeconds(1))
                .map(stockPrice -> "Stock Price Update: " + stockPrice)
                .publishOn(Schedulers.boundedElastic());
    }

    @PostMapping("/update")
    public void updateStockPrice(@RequestBody String stockPrice) {
        // Here, the controller can receive stock price updates from the frontend
        // and publish them to the Kafka topic
        stockPriceProducerConfig.sendStockPrice("AAPL", stockPrice);
        myStringSink.next("APPL-"+stockPrice);
    }

    @GetMapping("/stream2")
    public Flux<String> streamStockPrices2() {
        return myStringSink.getFlux()
                ;
    }

    @GetMapping("/fetch/{stock}")
    public Mono<StockPriceResponse> fetchStockPrice(@PathVariable String stock) {
        Mono<StockPriceResponse> stockPriceResponseMono = stockPriceFetcher.fetchStockPrice(stock);
        return stockPriceResponseMono;
    }
}
