package com.casamancaise.mapping;

import com.casamancaise.dto.DetailVenteDto;
import com.casamancaise.entities.DetailVente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VenteMapper.class, ArticleMapper.class})
public interface DetailVenteMapper extends EntityMapper<DetailVenteDto, DetailVente> {

    @Mapping(source = "vente.id", target = "venteId")
    @Mapping(source = "article.idArticle", target = "articleId")
    DetailVenteDto toDto(DetailVente detailVente);
    @Mapping(source = "venteId", target = "vente")
    @Mapping(source = "articleId", target = "article")
    DetailVente toEntity(DetailVenteDto detailVenteDto);
}