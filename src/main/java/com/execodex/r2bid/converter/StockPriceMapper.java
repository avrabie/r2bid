package com.execodex.r2bid.converter;


import com.execodex.r2bid.entity.Stock;
import com.execodex.r2bid.model.StockPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StockPriceMapper {

//    StockPriceMapper INSTANCE = Mappers.getMapper(StockPriceMapper.class);

    @Mapping(source = "open", target = "openPrice")
    @Mapping(source = "high", target = "highPrice")
    @Mapping(source = "low", target = "lowPrice")
    @Mapping(source = "close", target = "closePrice")
    @Mapping(source = "volume", target = "volume")
    Stock stockPriceToStock(StockPrice stockPrice);
}

