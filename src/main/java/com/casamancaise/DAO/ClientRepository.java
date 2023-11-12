package com.casamancaise.DAO;

import com.casamancaise.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer>{
}
