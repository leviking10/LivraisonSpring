package com.casamancaise.dao;

import com.casamancaise.entities.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenteRepository extends JpaRepository<Vente, Long> {
    // Ici pour ajouter des méthodes de requête personnalisées si nécessaire
}