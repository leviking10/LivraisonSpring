package com.casamancaise.mapping;

import com.casamancaise.dto.MouvementSortieDetailDto;
import com.casamancaise.entities.MouvementSortieDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArticleMapper.class, MouvementSortieMapper.class})
public interface MouvementSortieDetailMapper extends EntityMapper<MouvementSortieDetailDto, MouvementSortieDetail> {
    @Mapping(source = "article.idArticle", target = "articleId")
    @Mapping(source = "mouvementSortie.id", target = "mouvementSortieId")
    MouvementSortieDetailDto toDto(MouvementSortieDetail mouvementSortieDetail);

    @Mapping(source = "articleId", target = "article")
    @Mapping(source = "mouvementSortieId", target = "mouvementSortie")
    MouvementSortieDetail toEntity(MouvementSortieDetailDto dto);
}