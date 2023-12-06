package com.casamancaise.mapping;

import com.casamancaise.dto.ArticleDto;
import com.casamancaise.dto.ReceptionDetailDto;
import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.entities.Article;
import com.casamancaise.entities.Etat;
import com.casamancaise.entities.ReceptionDetail;
import com.casamancaise.entities.ReceptionStock;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ArticleMapper.class})
public interface ReceptionDetailMapper  extends EntityMapper<ReceptionDetailDto, ReceptionDetail>{
    @Mapping(source = "receptionStock.id",target = "idreceptionStock")
    @Mapping(source = "article.idArticle",target = "idarticle")
    ReceptionDetailDto toDto(ReceptionDetail entity);

    @Mapping(source = "idreceptionStock",target = "receptionStock")
    @Mapping(source = "idarticle",target = "article")
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
    default ReceptionStock map(Long id) {
        if (id == null) {
            return null;
        }
        ReceptionStock receptionStock = new ReceptionStock();
        receptionStock.setId(id);
        return receptionStock;
    }
    @AfterMapping
    default void updateFromDto(ReceptionDetailDto dto, @MappingTarget ReceptionDetail entity) {

        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getEtat() != null) {
            entity.setEtat(dto.getEtat());
        }

    }
}