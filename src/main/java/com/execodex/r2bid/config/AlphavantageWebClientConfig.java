package com.execodex.r2bid.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AlphavantageWebClientConfig {
    @Value("${r2bid.alphavantage.base-url}")
    private String baseUrl;

    @Bean
    @Qualifier("alphaVantageWebClient")
    public WebClient alphaVantageWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(baseUrl).build();
    }
}
