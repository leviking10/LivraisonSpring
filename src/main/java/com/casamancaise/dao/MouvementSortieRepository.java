package com.casamancaise.dao;

import com.casamancaise.entities.MouvementSortie;
import com.casamancaise.entities.ReceptionStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface MouvementSortieRepository extends JpaRepository<MouvementSortie,Long>{
    @Query(value = "SELECT COUNT(*) FROM mouvement_sortie WHERE CONVERT(date, date_sortie) = :today", nativeQuery = true)
    int countMvtSortie(LocalDate today);
    Optional<MouvementSortie> findByReference(String reference);
}

