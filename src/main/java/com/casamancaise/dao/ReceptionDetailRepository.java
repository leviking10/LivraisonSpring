package com.casamancaise.dao;

import com.casamancaise.entities.ReceptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ReceptionDetailRepository extends JpaRepository<ReceptionDetail,Long> {
}
