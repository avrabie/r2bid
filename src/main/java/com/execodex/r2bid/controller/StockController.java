package com.execodex.r2bid.controller;

import com.execodex.r2bid.kafka.StockPriceProducer;
import com.execodex.r2bid.sinks.MyStringSink;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@RestController
@RequestMapping("/api/stock")
public class StockController {


    private final StockPriceProducer stockPriceProducer;
    private final MyStringSink myStringSink;

    public StockController(StockPriceProducer stockPriceProducer, MyStringSink myStringSink) {
        this.stockPriceProducer = stockPriceProducer;
        this.myStringSink = myStringSink;
    }

    @GetMapping("/stream")
    public Flux<String> streamStockPrices() {
        return Flux.create(sink -> {
                    // Here, the producer can stream stock data to the frontend (SSE)
                    stockPriceProducer.sendStockPrice("AAPL", "150.50");
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
        stockPriceProducer.sendStockPrice("AAPL", "155.75");
        myStringSink.next("AAPL - 155.75");
    }

    @GetMapping("/stream2")
    public Flux<String> streamStockPrices2() {
        System.out.println("streamStockPrices2");
        return myStringSink.getFlux()
                ;
    }
}
