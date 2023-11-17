package com.casamancaise.mapping;
import com.casamancaise.dto.DetailsStockDto;
import com.casamancaise.entities.DetailsStock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetailsStockMapper {
    DetailsStockDto detailStockDto(DetailsStock detailStock);
    DetailsStock detailStockDtoToDetailStock(DetailsStockDto detailStockDto);
}
