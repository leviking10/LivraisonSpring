package com.casamancaise.services;
import com.casamancaise.dao.ArticleRepository;
import com.casamancaise.dto.ArticleDto;
import com.casamancaise.entities.Article;
import com.casamancaise.mapping.ArticleMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }
    @Override
    @Transactional
    public ArticleDto createArticle(ArticleDto articleDto) {
        Article article = articleMapper.articleDTOToArticle(articleDto);
        article = articleRepository.save(article);
        return articleMapper.articleToArticleDTO(article);
    }
    @Override
    @Transactional(readOnly = true)
    public ArticleDto getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("cette article n'existe pas" + id));
        return articleMapper.articleToArticleDTO(article);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new EntityNotFoundException("impossible de supprimer cette article : " + id);
        }
        articleRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ArticleDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(articleMapper::articleToArticleDTO)
                .toList();
    }

    @Override
    @Transactional
    public ArticleDto updateArticle(Long id, ArticleDto articleDto) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Impossible de mettre à jour cette article id: " + id));
        // Utilisez le mapper pour mettre à jour les champs de l'article existant.
        articleMapper.updateArticleFromDto(articleDto, existingArticle);
        // Sauvegardez les modifications
        Article updatedArticle = articleRepository.save(existingArticle);
        // Renvoyez le DTO mis à jour
        return articleMapper.articleToArticleDTO(updatedArticle);
    }

}