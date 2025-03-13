package com.execodex.r2bid.service;

import com.execodex.r2bid.model.StockPrice;
import com.execodex.r2bid.model.StockPriceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockPriceProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${r2bid.kafka.stock-topic}")
    private String stockTopic;

    public StockPriceProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStockPrice(String symbol, StockPriceResponse stockPriceResponse) {
        // Convert StockPrice object to JSON string
            String stockPriceJson;
        try {
            stockPriceJson = objectMapper.writeValueAsString(stockPriceResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(stockTopic, symbol, stockPriceJson);

    }
}

