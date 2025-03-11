package com.execodex.r2bid.service;

import com.execodex.r2bid.model.StockPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockPriceProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${r2bid.kafka.stock-topic}")
    private String stockTopic = "stock-topic";

    public StockPriceProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStockPrice(String symbol, StockPrice stockPrice) {
        kafkaTemplate.send(stockTopic, symbol, stockPrice.getClose().toString());
        log.info("Sent stock update for {}: {}", symbol, stockPrice.getClose());
    }
}

