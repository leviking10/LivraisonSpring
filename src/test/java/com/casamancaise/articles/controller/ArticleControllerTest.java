package com.casamancaise.articles.controller;
import com.casamancaise.controller.ArticleController;
import com.casamancaise.dto.ArticleDto;
import com.casamancaise.services.ArticleService;
import com.casamancaise.services.ArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@Tag("ArticleControllerTest")
@DisplayName("Testing du controller des articles")
class ArticleControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Mock
    private ArticleServiceImpl articleService;

    @InjectMocks
    private ArticleController articleController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createArticleTest() throws Exception {
        // Given
        ArticleDto articleDto = new ArticleDto(1L, "CPACK1L5", "Pack de 1.5L", 2000.0, "Pack de 6", 3000.0, 9.2);
        given(articleService.createArticle(any(ArticleDto.class))).willReturn(articleDto);

        // When & Then
        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idArticle").value(articleDto.getIdArticle()))
                .andExpect(jsonPath("$.refArti").value(articleDto.getRefArti()))
                .andExpect(jsonPath("$.libArti").value(articleDto.getLibArti()))
                .andExpect(jsonPath("$.coutProd").value(articleDto.getCoutProd()))
                .andExpect(jsonPath("$.unite").value(articleDto.getUnite()))
                .andExpect(jsonPath("$.margeSecu").value(articleDto.getMargeSecu()))
                .andExpect(jsonPath("$.tonage").value(articleDto.getTonage()));

        verify(articleService).createArticle(any(ArticleDto.class));
    }
    @Test
    void getArticleByIdTest() throws Exception {
        // Given
        ArticleDto articleDto = new ArticleDto(1L, "CPACK1L5", "Pack de 1.5L", 2000.0, "Pack de 6", 3000.0, 9.2);
        given(articleService.getArticleById(1L)).willReturn(articleDto);

        // When & Then
        mockMvc.perform(get("/api/articles/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idArticle").value(articleDto.getIdArticle()))
                .andExpect(jsonPath("$.refArti").value(articleDto.getRefArti()));

        verify(articleService).getArticleById(1L);
    }
    @Test
    void getAllArticlesTest() throws Exception {
        // Given
        ArticleDto articleDto = new ArticleDto(1L, "CPACK1L5", "Pack de 1.5L", 2000.0, "Pack de 6", 3000.0, 9.2);
        List<ArticleDto> allArticles = Arrays.asList(articleDto);
        given(articleService.getAllArticles()).willReturn(allArticles);

        // When & Then
        mockMvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idArticle").value(articleDto.getIdArticle()))
                .andExpect(jsonPath("$[0].refArti").value(articleDto.getRefArti()));

        verify(articleService).getAllArticles();
    }

    @Test
    void updateArticleTest() throws Exception {
        // Given
        ArticleDto articleDto = new ArticleDto(1L, "CPACK1L5", "Pack de 1.5L", 100.0, "Pack de 6", 3000.0, 9.2);
        given(articleService.updateArticle(eq(1L), any(ArticleDto.class))).willReturn(articleDto);

        // When & Then
        mockMvc.perform(put("/api/articles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idArticle").value(articleDto.getIdArticle()))
                .andExpect(jsonPath("$.refArti").value(articleDto.getRefArti()));

        verify(articleService).updateArticle(eq(1L), any(ArticleDto.class));
    }

    @Test
    void deleteArticleTest() throws Exception {
        // Given
        willDoNothing().given(articleService).deleteArticle(1L);

        // When & Then
        mockMvc.perform(delete("/api/articles/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(articleService).deleteArticle(1L);
    }

}
