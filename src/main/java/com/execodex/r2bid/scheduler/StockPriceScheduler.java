package com.execodex.r2bid.scheduler;

import com.execodex.r2bid.model.StockPrice;
import com.execodex.r2bid.service.StockPriceFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockPriceScheduler {

    private final StockPriceFetcher stockPriceFetcher;

    public StockPriceScheduler(StockPriceFetcher stockPriceFetcher) {
        this.stockPriceFetcher = stockPriceFetcher;
    }

    @Scheduled(fixedRate = 60000*10) // Fetch every 60 sec
    public void fetchStockData() {
        stockPriceFetcher.fetchStockPrice("TSLA")
                .subscribe(response -> {
                    String latestTimestamp = response.getTimeSeries().keySet().iterator().next();
                    StockPrice latestPrice = response.getTimeSeries().get(latestTimestamp);

                    log.info("Stock: {}, Time: {}, Price: {}",
                            response.getMetaData().getSymbol(),
                            latestTimestamp,
                            latestPrice.getClose());
                });
    }
}

