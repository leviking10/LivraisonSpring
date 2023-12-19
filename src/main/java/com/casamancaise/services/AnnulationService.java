package com.casamancaise.services;

import com.casamancaise.dto.AnnulationDto;
import com.casamancaise.dto.ReceptionStockDto;

import java.util.List;
import java.util.Optional;

public interface AnnulationService {
    AnnulationDto createAnnulation(AnnulationDto annulationDto);
    AnnulationDto getAnnulationById(Long id);
    List<AnnulationDto> getAllAnnulations();
    Optional<AnnulationDto> findByRef(String reference);
}