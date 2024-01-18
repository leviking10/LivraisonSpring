package com.casamancaise.dao;
import com.casamancaise.entities.Retour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RetourRepository extends JpaRepository<Retour, Long> {
    @Query(value = "SELECT COUNT(*) FROM retour WHERE CONVERT(date, date_retour) = CONVERT(date, GETDATE())", nativeQuery = true)
    int countRetoursForToday();
    Optional<Retour> findByReference(String reference);
    @Query("SELECT r FROM Retour r JOIN r.detailsRetours dr WHERE dr.article.idArticle = :articleId AND r.operationId = :operationId")
    List<Retour> findAllRetoursByArticleAndOperation(@Param("articleId") Long articleId, @Param("operationId") Long operationId);
}