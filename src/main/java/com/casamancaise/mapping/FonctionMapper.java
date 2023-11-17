package com.casamancaise.mapping;

import com.casamancaise.dto.FonctionDto;
import com.casamancaise.entities.Fonction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FonctionMapper {
    FonctionDto fonctionToFonctionDTO(Fonction fonction);
    Fonction fonctionDTOToFonction(FonctionDto fonctionDTO);
    @Mapping(target = "idFonc", ignore = true)
    void updateFonctionFromDto(FonctionDto dto, @MappingTarget Fonction entity);
    default Fonction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Fonction fonction = new Fonction();
        fonction.setIdFonc(id);
        return fonction;
    }
}
