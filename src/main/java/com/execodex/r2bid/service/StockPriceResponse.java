package com.execodex.r2bid.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class StockPriceResponse {
    @JsonProperty("Meta Data")
    private MetaData metaData;

    @JsonProperty("Time Series (1min)")
    private Map<String, StockPrice> timeSeries;
}

@Data
class MetaData {
    @JsonProperty("1. Information")
    private String information;
    @JsonProperty("2. Symbol")
    private String symbol;
    @JsonProperty("3. Last Refreshed")
    private String lastRefreshed;
    @JsonProperty("4. Interval")
    private String interval;
    @JsonProperty("5. Output Size")
    private String outputSize;
    @JsonProperty("6. Time Zone")
    private String timezone;
}

@Data
class StockPrice {
    @JsonProperty("1. open")
    private BigDecimal open;

    @JsonProperty("2. high")
    private BigDecimal high;

    @JsonProperty("3. low")
    private BigDecimal low;

    @JsonProperty("4. close")
    private BigDecimal close;

    @JsonProperty("5. volume")
    private Long volume;
}

