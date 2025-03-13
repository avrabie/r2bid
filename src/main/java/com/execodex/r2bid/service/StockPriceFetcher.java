package com.execodex.r2bid.service;

import com.execodex.r2bid.model.StockPriceResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StockPriceFetcher {

    private final WebClient webClient;

    @Value("${r2bid.alphavantage.api-key}")
    private String API_KEY = "YOUR_API_KEY";
    @Value("${r2bid.alphavantage.interval}")
    private String interval = "1min";


    public StockPriceFetcher(@Qualifier("alphaVantageWebClient") WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<StockPriceResponse> fetchStockPrice(String symbol) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "TIME_SERIES_INTRADAY")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", interval)
                        .queryParam("apikey", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(StockPriceResponse.class);
    }



}

