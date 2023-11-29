package com.casamancaise.dao;

import com.casamancaise.entities.Mouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MouvementRepository extends JpaRepository<Mouvement,Integer> {
    @Query("SELECT m FROM Mouvement m WHERE m.inventaire.entrepot.idEntre = :entrepotId")
    List<Mouvement> findByEntrepotId(@Param("entrepotId") int entrepotId);
    @Query("SELECT m FROM Mouvement m WHERE m.inventaire.article.idArticle = :articleId")
    List<Mouvement> findByArticleId(@Param("articleId") Long articleId);
    List<Mouvement> findByDateMouvementBetween(LocalDate startDate, LocalDate endDate);
}
