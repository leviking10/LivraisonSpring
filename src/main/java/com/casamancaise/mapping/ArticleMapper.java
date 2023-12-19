package com.casamancaise.mapping;
import com.casamancaise.dto.ArticleDto;
import com.casamancaise.entities.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface ArticleMapper{

    ArticleDto toDto(Article entity);
    Article toEntity(ArticleDto dto);
    default Article fromId(Long id) {
        if (id == null) {
            return null;
        }
        Article article = new Article();
        article.setIdArticle(id);
        return article;
    }
    @Mapping(target = "idArticle", ignore = true)
    void updateArticleFromDto(ArticleDto dto, @MappingTarget Article entity);
}