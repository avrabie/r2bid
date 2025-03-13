package com.execodex.r2bid.controller;

import com.execodex.r2bid.entity.Stock;
import com.execodex.r2bid.model.StockPriceResponse;
import com.execodex.r2bid.repository.StockRepository;
import com.execodex.r2bid.scheduler.StockPriceScheduler;
import com.execodex.r2bid.service.StockPriceFetcher;
import com.execodex.r2bid.sinks.MyStringSink;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stock")
public class StockController {


    private final MyStringSink myStringSink;
    private final StockPriceFetcher stockPriceFetcher;
    private final StockRepository stockRepository;

    public StockController(MyStringSink myStringSink, StockPriceFetcher stockPriceFetcher, StockRepository stockRepository) {
        this.myStringSink = myStringSink;
        this.stockPriceFetcher = stockPriceFetcher;
        this.stockRepository = stockRepository;
    }


    @PostMapping("/bid/{ticker}")
    public void updateStockPrice(@RequestBody String stockPrice, @PathVariable String ticker) {
        myStringSink.next(ticker + "-" + stockPrice);
    }

    @GetMapping("/myStream")
    public Flux<String> streamMyStream() {
        return myStringSink.getFlux()
                ;
    }

    @GetMapping("/fetch/{stock}")
    public Mono<StockPriceResponse> fetchStockPrice(@PathVariable String stock) {
        Mono<StockPriceResponse> stockPriceResponseMono = stockPriceFetcher.fetchStockPrice(stock);
        return stockPriceResponseMono;
    }

    @PutMapping("/update/{stock}")
    public Mono<String> updateStockTicker(@PathVariable String stock) {
        StockPriceScheduler.setStockTicker(stock);
        myStringSink.next("Stock ticker updated to: " + stock);
        return Mono.just("Stock ticker updated to: " + stock);
    }

    // ✅ Get all stocks (reactive stream)
    @GetMapping
    public Flux<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    // ✅ Get stock by symbol
    @GetMapping("/{symbol}")
    public Flux<Stock> getStocksBySymbol(@PathVariable String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    // ✅ Get latest stock price for a symbol
    @GetMapping("/{symbol}/latest")
    public Mono<Stock> getLatestStock(@PathVariable String symbol) {
        return stockRepository.findTopBySymbolOrderByTimestampDesc(symbol);
    }

    // ✅ Fetch paginated historical stock data
    @GetMapping("/{symbol}/history")
    public Flux<Stock> getStockHistory(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page
    ) {
        int offset = page * size;
        return stockRepository.findBySymbolPaged(symbol, size, offset);
    }


//    @GetMapping("/stream")
//    public Flux<String> streamStockPrices() {
//        return Flux.create(sink -> {
//                    // Here, the producer can stream stock data to the frontend (SSE)
//                    stockPriceProducerConfig.sendStockPrice("AAPL", "150.50");
//                    sink.next("AAPL - 150.50");
//                    // Implement continuous streaming logic to push new stock prices to the frontend
//                }).delayElements(Duration.ofSeconds(1))
//                .map(stockPrice -> "Stock Price Update: " + stockPrice)
//                .publishOn(Schedulers.boundedElastic());
//    }
}
