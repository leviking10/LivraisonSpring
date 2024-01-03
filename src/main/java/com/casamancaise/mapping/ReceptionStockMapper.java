package com.casamancaise.mapping;

import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.entities.ReceptionStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EntrepotMapper.class, ReceptionDetailMapper.class})
public interface ReceptionStockMapper extends EntityMapper<ReceptionStockDto, ReceptionStock> {
    @Mapping(source = "entrepot.idEntre", target = "entrepotId")
    ReceptionStockDto toDto(ReceptionStock entity);

    @Mapping(source = "entrepotId", target = "entrepot")
    ReceptionStock toEntity(ReceptionStockDto receptionStockDto);

    default ReceptionStock fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReceptionStock receptionStock = new ReceptionStock();
        receptionStock.setId(id);
        return receptionStock;
    }

    void updateFromDto(ReceptionStockDto dto, @MappingTarget ReceptionStock entity);
}