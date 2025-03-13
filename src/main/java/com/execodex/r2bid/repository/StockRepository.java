package com.execodex.r2bid.repository;

import com.execodex.r2bid.entity.Stock;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StockRepository  extends ReactiveCrudRepository<Stock, UUID> {
    Flux<Stock> findBySymbol(String symbol);

    Mono<Stock> findTopBySymbolOrderByTimestampDesc(String symbol);

    // ✅ Paginate stock prices for a symbol
    @Query("SELECT * FROM stocks WHERE symbol = :symbol ORDER BY timestamp DESC LIMIT :size OFFSET :offset")
    Flux<Stock> findBySymbolPaged(String symbol, int size, int offset);
}
