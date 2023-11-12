package com.casamancaise.mapping;

import com.casamancaise.DTO.StockUsineDto;
import com.casamancaise.Entities.StockUsine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockUsineMapper {
    StockUsineDto stockUsineToStockUsineDTO(StockUsine stockUsine);
    StockUsine stockUsineDTOToStockUsine(StockUsineDto stockUsineDTO);
}
