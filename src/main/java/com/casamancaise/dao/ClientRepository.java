package com.casamancaise.dao;

import com.casamancaise.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long>{
    @Query("SELECT c FROM Client c WHERE c.canalDistrib.canal = 'Grossiste'")
    List<Client> findClientsByGrossiste();

}
