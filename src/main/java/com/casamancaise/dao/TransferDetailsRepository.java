package com.casamancaise.dao;

import com.casamancaise.entities.TransferDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferDetailsRepository extends JpaRepository<TransferDetails,Long> {
}
