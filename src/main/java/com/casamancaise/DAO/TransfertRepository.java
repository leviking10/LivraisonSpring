package com.casamancaise.DAO;

import com.casamancaise.Entities.Transfert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransfertRepository extends JpaRepository<Transfert, Long> {
}
