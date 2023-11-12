package com.casamancaise.mapping;
import com.casamancaise.DTO.DetailsStockDto;
import com.casamancaise.Entities.DetailsStock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetailsStockMapper {
    DetailsStockDto detailStockDto(DetailsStock detailStock);
    DetailsStock detailStockDtoToDetailStock(DetailsStockDto detailStockDto);
}
