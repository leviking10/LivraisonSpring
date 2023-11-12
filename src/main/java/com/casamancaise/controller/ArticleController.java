package com.casamancaise.controller;

import com.casamancaise.DTO.ArticleDto;
import com.casamancaise.Services.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDto createArticle(@RequestBody @Valid ArticleDto articleDto) {
        return articleService.createArticle(articleDto);
    }

    @GetMapping("/{id}")
    public ArticleDto getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping
    public List<ArticleDto> getAllArticles() {
        return articleService.getAllArticles();
    }

    @PutMapping("/{id}")
    public ArticleDto updateArticle(@PathVariable Long id, @RequestBody @Valid ArticleDto articleDto) {
        return articleService.updateArticle(id, articleDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
}