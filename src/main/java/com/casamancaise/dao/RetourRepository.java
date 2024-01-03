package com.casamancaise.dao;
import com.casamancaise.entities.Retour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RetourRepository extends JpaRepository<Retour, Long> {
    @Query(value = "SELECT COUNT(*) FROM retour WHERE CONVERT(date, date_retour) = CONVERT(date, GETDATE())", nativeQuery = true)
    int countRetoursForToday();
    Optional<Retour> findByReference(String reference);
}