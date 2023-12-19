package com.casamancaise.mapping;

import com.casamancaise.dto.DetailRetourDto;
import com.casamancaise.entities.DetailRetour;
import com.casamancaise.entities.Retour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ArticleMapper.class, RetourMapper.class})
public interface DetailRetourMapper extends EntityMapper<DetailRetourDto, DetailRetour> {
    @Override
    @Mapping(source = "retour.id", target = "retourId")
    @Mapping(source = "article.idArticle", target = "articleId")
    DetailRetourDto toDto(DetailRetour entity);

    @Override
    @Mapping(source = "retourId", target = "retour")
    @Mapping(source = "articleId", target = "article")
    DetailRetour toEntity(DetailRetourDto dto);
    @Override
    List<DetailRetourDto> toDto(List<DetailRetour> entityList);

    @Override
    List<DetailRetour> toEntity(List<DetailRetourDto> dtoList);
}
