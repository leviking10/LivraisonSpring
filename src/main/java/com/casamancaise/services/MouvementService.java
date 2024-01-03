package com.casamancaise.services;

import com.casamancaise.dto.MouvementDto;

import java.time.LocalDate;
import java.util.List;

public interface MouvementService {
    MouvementDto recordMovement(MouvementDto mouvementDto);

    List<MouvementDto> getMovementsByArticle(Long articleId);

    List<MouvementDto> getMovementsByEntrepot(Integer entrepotId);

    List<MouvementDto> getMovementsByDateRange(LocalDate startDate, LocalDate endDate);
}
