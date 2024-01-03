package com.casamancaise.mapping;

import com.casamancaise.dto.RetourDto;
import com.casamancaise.entities.Retour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DetailRetourMapper.class, VehiculeMapper.class})
public interface RetourMapper extends EntityMapper<RetourDto, Retour> {

    @Mapping(target = "entrepotId", source = "entrepot.idEntre")
    @Mapping(target = "vehiculeId", source = "vehicule.idVehi")
    RetourDto toDto(Retour entity);

    @Mapping(target = "entrepot.idEntre", source = "entrepotId")
    @Mapping(target = "vehicule.idVehi", source = "vehiculeId")
    Retour toEntity(RetourDto dto);

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