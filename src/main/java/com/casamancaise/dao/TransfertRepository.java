package com.casamancaise.dao;

import com.casamancaise.entities.ReceptionStock;
import com.casamancaise.entities.Transfert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransfertRepository extends JpaRepository<Transfert, Long> {
    @Query(value = "SELECT COUNT(*) FROM transfert WHERE CONVERT(date, transfer_date) = CONVERT(date, GETDATE())", nativeQuery = true)
    int countTransfert();
    Optional<Transfert> findByReference(String reference);
}
