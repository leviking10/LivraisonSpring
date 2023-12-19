package com.casamancaise.mapping;

import com.casamancaise.dto.MouvementDto;
import com.casamancaise.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring",uses = InventaireMapper.class)
public interface MouvementMapper extends EntityMapper<MouvementDto, Mouvement> {
    @Override
    @Mapping(source = "inventaire.id", target = "inventaireId")
    MouvementDto toDto(Mouvement mouvement);
    @Override
    @Mapping(source = "inventaireId", target = "inventaire")
    Mouvement toEntity(MouvementDto dto);
}
