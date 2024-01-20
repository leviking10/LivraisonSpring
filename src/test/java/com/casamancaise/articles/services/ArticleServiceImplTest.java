package com.casamancaise.articles.services;
import com.casamancaise.dao.ArticleRepository;
import com.casamancaise.dto.ArticleDto;
import com.casamancaise.entities.Article;
import com.casamancaise.mapping.ArticleMapper;
import com.casamancaise.services.ArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
 class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleMapper articleMapper;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void createArticleTest() {
        // Arrange
        ArticleDto articleDto = new ArticleDto(1L, "CPACK1L5", "Pack de 1.5L", 2000.0, "Pack de 6", 3000.0, 9.2);
        Article article = new Article(); // Set properties as needed
        given(articleMapper.toEntity(any(ArticleDto.class))).willReturn(article);
        given(articleRepository.save(any(Article.class))).willReturn(article);
        given(articleMapper.toDto(any(Article.class))).willReturn(articleDto);

        // Act
        ArticleDto savedArticleDto = articleService.createArticle(articleDto);

        // Assert
        assertNotNull(savedArticleDto);
        assertEquals(articleDto.getRefArti(), savedArticleDto.getRefArti());

        verify(articleRepository).save(any(Article.class));
        verify(articleMapper).toDto(any(Article.class));
    }

    @Test
    public void getArticleByIdTest() {
        // Arrange
        Long id = 1L;
        ArticleDto articleDto = new ArticleDto(id, "CPACK1L5", "Pack de 1.5L", 2000.0, "Pack de 6", 3000.0, 9.2);
        Article article = new Article(); // Set properties as needed
        given(articleRepository.findById(id)).willReturn(Optional.of(article));
        given(articleMapper.toDto(any(Article.class))).willReturn(articleDto);

        // Act
        ArticleDto foundArticleDto = articleService.getArticleById(id);

        // Assert
        assertNotNull(foundArticleDto);
        assertEquals(articleDto.getIdArticle(), foundArticleDto.getIdArticle());

        verify(articleRepository).findById(id);
        verify(articleMapper).toDto(any(Article.class));
    }

    @Test
    void getArticleByIdNotFoundTest() {
        // Arrange
        Long id = 1L;
        given(articleRepository.findById(id)).willReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> articleService.getArticleById(id));
        assertTrue(exception.getMessage().contains("Article not found with id: " + id));
    }
    @Test
    void getAllArticlesTest() {
        // Given
        ArticleDto articleDto = new ArticleDto(1L, "CPACK1L5", "Pack de 1.5L", 2000.0, "Pack de 6", 3000.0, 9.2);
        Article article = new Article(); // Set properties as needed
        List<Article> articles = Collections.singletonList(article);
        given(articleRepository.findAll()).willReturn(articles);
        given(articleMapper.toDto(any(Article.class))).willReturn(articleDto);

        // Act
        List<ArticleDto> articleDtos = articleService.getAllArticles();

        // Assert
        assertNotNull(articleDtos);
        assertFalse(articleDtos.isEmpty(), "The list of articles should not be empty");
        assertEquals(1, articleDtos.size(), "The t of articles should contain one article");

        verify(articleRepository).findAll();
        verify(articleMapper, times(1)).toDto(any(Article.class));
    }

    @Test
    public void updateArticleTest() {
        // Arrange
        Long id = 1L;
        ArticleDto articleDto = new ArticleDto(id, "CPACK1L5", "Pack de 1.5L", 2000.0, "Pack de 6", 3000.0, 9.2);
        Article article = new Article(); // Set properties as needed
        given(articleRepository.findById(id)).willReturn(Optional.of(article));
        given(articleRepository.save(any(Article.class))).willReturn(article);
        given(articleMapper.toDto(any(Article.class))).willReturn(articleDto);

        // Act
        ArticleDto updatedArticleDto = articleService.updateArticle(id, articleDto);

        // Assert
        assertNotNull(updatedArticleDto);
        assertEquals(articleDto.getIdArticle(), updatedArticleDto.getIdArticle());

        verify(articleRepository).findById(id);
        verify(articleRepository).save(any(Article.class));
        verify(articleMapper).toDto(any(Article.class));
    }

    @Test
    public void deleteArticleTest() {
        // Arrange
        Long id = 1L;
        willDoNothing().given(articleRepository).deleteById(id);

        // Act
        articleService.deleteArticle(id);

        // Assert
        verify(articleRepository).deleteById(id);
    }
}
