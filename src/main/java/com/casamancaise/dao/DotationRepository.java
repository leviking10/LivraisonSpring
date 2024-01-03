package com.casamancaise.dao;
import com.casamancaise.entities.Dotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DotationRepository extends JpaRepository<Dotation, Long> {
    @Query(value = "SELECT COUNT(*) FROM dotations WHERE CONVERT(date, date_dotation) = CONVERT(date, GETDATE())", nativeQuery = true)
    int countDotationForToday();
    Optional<Dotation> findByReference(String reference);
}
