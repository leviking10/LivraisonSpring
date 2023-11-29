package com.casamancaise.services;

import com.casamancaise.dto.ReceptionStockDto;

import java.util.List;

public interface ReceptionService {
    ReceptionStockDto saveReception(ReceptionStockDto receptionStockDto);
    ReceptionStockDto getReceptionById(Long id);
    void deleteReception(Long id);
    List<ReceptionStockDto> getAllReceptions();
    ReceptionStockDto updateReception(Long id, ReceptionStockDto receptionStockDto);
}
