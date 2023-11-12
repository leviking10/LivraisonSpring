package com.casamancaise.DAO;

import com.casamancaise.Entities.DetailCommande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailCommandeRepository extends JpaRepository<DetailCommande, Long> {
}
