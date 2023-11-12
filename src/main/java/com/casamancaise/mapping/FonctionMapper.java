package com.casamancaise.mapping;

import com.casamancaise.DTO.ArticleDto;
import com.casamancaise.DTO.FonctionDto;
import com.casamancaise.Entities.Article;
import com.casamancaise.Entities.Fonction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FonctionMapper {
    FonctionDto fonctionToFonctionDTO(Fonction fonction);
    Fonction fonctionDTOToFonction(FonctionDto fonctionDTO);
    @Mapping(target = "idFonc", ignore = true)
    void updateFonctionFromDto(FonctionDto dto, @MappingTarget Fonction entity);
}
