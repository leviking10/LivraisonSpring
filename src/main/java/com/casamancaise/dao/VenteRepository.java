package com.casamancaise.dao;
import com.casamancaise.entities.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VenteRepository extends JpaRepository<Vente, Long> {
    @Query(value = "SELECT COUNT(*) FROM ventes WHERE CONVERT(date, date_vente) = CONVERT(date, GETDATE())", nativeQuery = true)
    int countVenteForToday();

    Optional<Vente> findByReference(String reference);
}