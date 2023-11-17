package com.casamancaise.dao;

import com.casamancaise.entities.Transfert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransfertRepository extends JpaRepository<Transfert, Long> {
}
