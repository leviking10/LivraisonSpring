package com.casamancaise.mapping;
import com.casamancaise.DTO.ArticleDto;
import com.casamancaise.Entities.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface ArticleMapper {
        ArticleDto articleToArticleDTO(Article article);
        Article articleDTOToArticle(ArticleDto articleDTO);
    @Mapping(target = "idArticle", ignore = true)
    void updateArticleFromDto(ArticleDto dto, @MappingTarget Article entity);

}