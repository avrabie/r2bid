package com.execodex.r2bid.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StockPriceFetcher {

    private final WebClient webClient;

    private final String API_KEY = "YOUR_API_KEY"; // Replace with actual key

    public StockPriceFetcher(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.alphavantage.co").build();
    }

    public Mono<StockPriceResponse> fetchStockPrice(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "TIME_SERIES_INTRADAY")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", "1min")
                        .queryParam("apikey", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }
}

