package com.execodex.r2bid.repository;

import com.execodex.r2bid.entity.Stock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface StockRepository  extends ReactiveCrudRepository<Stock, UUID> {
}
