package com.casamancaise.dao;

import com.casamancaise.entities.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventaireRepository extends JpaRepository<Inventaire,Long> {
    Optional<Inventaire> findByArticleIdArticleAndEntrepotIdEntre(Long idArticle, int idEntre);


}
