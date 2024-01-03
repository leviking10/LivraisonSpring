package com.casamancaise.mapping;

import com.casamancaise.dto.TransfertDto;
import com.casamancaise.entities.Transfert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TransfertDetailsMapper.class, VehiculeMapper.class})
public interface TransfertMapper extends EntityMapper<TransfertDto, Transfert> {

    @Mapping(target = "fromEntrepotId", source = "fromEntrepot.idEntre")
    @Mapping(target = "vehiculeId", source = "vehicule.idVehi")
    TransfertDto toDto(Transfert transfert);

    @Mapping(target = "fromEntrepot.idEntre", source = "fromEntrepotId")
    @Mapping(target = "vehicule.idVehi", source = "vehiculeId")
    Transfert toEntity(TransfertDto dto);
}
