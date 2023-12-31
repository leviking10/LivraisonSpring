package com.casamancaise.dao;
import com.casamancaise.entities.Annulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
public interface AnnulationRepository extends JpaRepository<Annulation,Long> {
    @Query(value = "SELECT COUNT(*) FROM annulation WHERE CONVERT(date, date_annulation) = CONVERT(date, GETDATE())", nativeQuery = true)
    int countAnnulationForToday();
    Optional<Annulation> findByRef(String reference);
}
