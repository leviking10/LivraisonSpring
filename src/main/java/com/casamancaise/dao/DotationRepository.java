package com.casamancaise.dao;
import com.casamancaise.entities.Dotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DotationRepository extends JpaRepository<Dotation, Long> {
}
