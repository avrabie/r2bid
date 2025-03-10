package com.execodex.r2bid.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StockPriceConsumer {


    @KafkaListener(topics = "stock-prices", groupId = "r2bid-group")
    public void consumeStockPrice(String message) {
        System.out.println("KafkaListener Received stock price: " + message);
        // TODO implement further logic to update the database or push to SSE
    }
}
