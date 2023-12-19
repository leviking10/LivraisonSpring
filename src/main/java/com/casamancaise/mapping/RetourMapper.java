package com.casamancaise.mapping;

import com.casamancaise.dto.RetourDto;
import com.casamancaise.entities.ReceptionStock;
import com.casamancaise.entities.Retour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DetailRetourMapper.class})
public interface RetourMapper extends EntityMapper<RetourDto, Retour> {

    @Override
    RetourDto toDto(Retour entity);

    @Override
    Retour toEntity(RetourDto dto);

    @Override
    List<RetourDto> toDto(List<Retour> entityList);

    @Override
    List<Retour> toEntity(List<RetourDto> dtoList);
    default Retour fromId(Long id) {
        if (id == null) {
            return null;
        }
        Retour retour = new Retour();
        retour.setId(id);
        return retour;
    }

    void updateFromDto(RetourDto dto, @MappingTarget Retour entity);
}