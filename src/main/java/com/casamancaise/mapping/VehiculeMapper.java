package com.casamancaise.mapping;

import com.casamancaise.dto.VehiculeDto;
import com.casamancaise.entities.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    VehiculeDto vehiculeToVehiculeDTO(Vehicule vehicule);
    Vehicule vehiculeDTOToVehicule(VehiculeDto vehiculeDto);
    @Mapping(target = "idVehi", ignore = true)
    void updateVehiculeFromDto(VehiculeDto dto, @MappingTarget Vehicule entity);
}
