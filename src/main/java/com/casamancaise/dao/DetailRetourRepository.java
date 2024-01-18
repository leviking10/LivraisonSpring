package com.casamancaise.dao;

import com.casamancaise.entities.DetailRetour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailRetourRepository extends JpaRepository<DetailRetour,Long> {
    @Query("SELECT dr FROM DetailRetour dr WHERE dr.article.idArticle = :articleId AND dr.retour.operationId = :operationId")
    List<DetailRetour> findAllByArticleIdAndOperationId(@Param("articleId") Long articleId, @Param("operationId") Long operationId);
}