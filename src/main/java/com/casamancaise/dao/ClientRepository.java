package com.casamancaise.dao;

import com.casamancaise.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long>{
    @Query("SELECT c FROM Client c WHERE c.canalDistrib.canal = 'Grossiste'")
    List<Client> findClientsByGrossiste();
    @Query("SELECT COUNT(c) > 0 FROM Client c WHERE c.id = :clientId AND c.canalDistrib.idCanal = :canalDistribId")
    boolean existsByIdAndCanalDistribId(@Param("clientId") Long clientId, @Param("canalDistribId") Integer canalDistribId);
}
