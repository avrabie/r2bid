package com.execodex.r2bid.kafka;

import com.execodex.r2bid.sinks.MyStringSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockPriceConsumer {

    private final MyStringSink myStringSink;

    public StockPriceConsumer(MyStringSink myStringSink) {
        this.myStringSink = myStringSink;
    }


    @KafkaListener(topics = "stock-prices", groupId = "r2bid-group")
    public void consumeStockPrice(String message) {
        myStringSink.next(message);
        log.info("KafkaListener Received stock price: {}", message);
        // TODO implement further logic to update the database or push to SSE
    }
}
