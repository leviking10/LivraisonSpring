package com.casamancaise.mapping;

import com.casamancaise.dto.DetailVenteDto;
import com.casamancaise.entities.DetailVente;
import com.casamancaise.entities.Dotation;
import com.casamancaise.entities.Vente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VenteMapper.class, ArticleMapper.class})
public interface DetailVenteMapper extends EntityMapper<DetailVenteDto, DetailVente> {

    @Mapping(source = "vente.id", target = "venteId")
    @Mapping(source = "article.idArticle", target = "articleId")
    @Override
    DetailVenteDto toDto(DetailVente detailVente);

    @Mapping(source = "venteId", target = "vente")
    @Mapping(source = "articleId", target = "article")
    @Override
    DetailVente toEntity(DetailVenteDto detailVenteDto);

}