package com.casamancaise.dao;

import com.casamancaise.entities.DetailCommande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailCommandeRepository extends JpaRepository<DetailCommande, Long> {
}
