package com.casamancaise.Services;
import com.casamancaise.DTO.ArticleDto;
import java.util.List;
public interface ArticleService {
        ArticleDto createArticle(ArticleDto articleDto);
        ArticleDto getArticleById(Long id);
        List<ArticleDto> getAllArticles();
        ArticleDto updateArticle(Long id, ArticleDto articleDto);
        void deleteArticle(Long id);
}

