package com.execodex.r2bid.scheduler;

import com.execodex.r2bid.model.StockPrice;
import com.execodex.r2bid.service.StockPriceFetcher;
import com.execodex.r2bid.service.StockPriceProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class StockPriceScheduler {

    private final StockPriceFetcher stockPriceFetcher;
    private final StockPriceProducer stockPriceProducer;

    public StockPriceScheduler(StockPriceFetcher stockPriceFetcher, StockPriceProducer stockPriceProducer) {
        this.stockPriceFetcher = stockPriceFetcher;
        this.stockPriceProducer = stockPriceProducer;
    }

    @Scheduled(fixedRate = 60000) // Fetch every 10 min
    public void fetchStockData() {
        stockPriceFetcher.fetchStockPrice("TSLA")
                .subscribe(response -> {
                    String latestTimestamp = response.getTimeSeries().keySet().iterator().next();
                    StockPrice latestPrice = response.getTimeSeries().get(latestTimestamp);

                    log.info("Stock: {}, Time: {}, Price: {}",
                            response.getMetaData().getSymbol(),
                            latestTimestamp,
                            latestPrice.getClose());
                    stockPriceProducer.sendStockPrice(response.getMetaData().getSymbol(), latestPrice);
                });
    }
}

