package com.casamancaise.DAO;

import com.casamancaise.Entities.TransferDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferDetailsRepository extends JpaRepository<TransferDetails,Long> {
}
