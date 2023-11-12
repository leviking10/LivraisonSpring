package com.casamancaise.mapping;

import com.casamancaise.DTO.ArticleDto;
import com.casamancaise.DTO.TransfertDto;
import com.casamancaise.DTO.VehiculeDto;
import com.casamancaise.Entities.Article;
import com.casamancaise.Entities.Transfert;
import com.casamancaise.Entities.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {
    VehiculeDto vehiculeToVehiculeDTO(Vehicule vehicule);
    Vehicule vehiculeDTOToVehicule(VehiculeDto vehiculeDto);
    @Mapping(target = "idVehi", ignore = true)
    void updateVehiculeFromDto(VehiculeDto dto, @MappingTarget Vehicule entity);
}
