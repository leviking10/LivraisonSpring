package com.casamancaise.mapping;

import com.casamancaise.dto.StockUsineDto;
import com.casamancaise.entities.StockUsine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockUsineMapper {
    StockUsineDto stockUsineToStockUsineDTO(StockUsine stockUsine);
    StockUsine stockUsineDTOToStockUsine(StockUsineDto stockUsineDTO);
}
