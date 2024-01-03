package com.casamancaise.mapping;

import com.casamancaise.dto.ReceptionDetailDto;
import com.casamancaise.entities.ReceptionDetail;
import com.casamancaise.enums.Etat;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ArticleMapper.class, ReceptionStockMapper.class})
public interface ReceptionDetailMapper extends EntityMapper<ReceptionDetailDto, ReceptionDetail> {
    @Mapping(source = "receptionStock.id", target = "idreceptionStock")
    @Mapping(source = "article.idArticle", target = "idarticle")
    ReceptionDetailDto toDto(ReceptionDetail entity);

    @Mapping(source = "idreceptionStock", target = "receptionStock")
    @Mapping(source = "idarticle", target = "article")
    ReceptionDetail toEntity(ReceptionDetailDto receptionDetailDto);

    default Etat map(String etat) {
        if (etat == null || etat.isEmpty()) {
            return Etat.CONFORME; // Valeur par défaut si la String est null ou vide
        }
        try {
            return Etat.valueOf(etat);
        } catch (IllegalArgumentException e) {
            return Etat.CONFORME; // Valeur par défaut si la String ne correspond à aucune valeur de l'enum
        }
    }

    @AfterMapping
    default void updateFromDto(ReceptionDetailDto dto, @MappingTarget ReceptionDetail entity) {

        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
    }
}