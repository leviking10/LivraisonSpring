package com.casamancaise.dao;
import com.casamancaise.entities.ReceptionStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReceptionStockRepository extends JpaRepository<ReceptionStock,Long> {
    @Query(value = "SELECT COUNT(*) FROM reception_stock WHERE CONVERT(date, date_reception) = CONVERT(date, GETDATE())", nativeQuery = true)
    int countReceptionsForToday();
    Optional<ReceptionStock> findByReference(String reference);
}
