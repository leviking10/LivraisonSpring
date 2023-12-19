package com.casamancaise.services;

import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.entities.ReceptionStock;

import java.util.List;
import java.util.Optional;

public interface ReceptionService {
    ReceptionStockDto saveReception(ReceptionStockDto receptionStockDto);
    ReceptionStockDto getReceptionById(Long id);
    void deleteReception(Long id);
    List<ReceptionStockDto> getAllReceptions();
    Optional<ReceptionStockDto> findByReference(String reference);
}
