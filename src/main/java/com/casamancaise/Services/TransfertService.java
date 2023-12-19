package com.casamancaise.services;
import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.dto.TransfertDto;

import java.util.List;
import java.util.Optional;

public interface TransfertService {

    TransfertDto saveTransfert(TransfertDto transfertDto);

    TransfertDto getTransfertById(Long id);

    List<TransfertDto> getAllTransferts();
    void deleteTransfert(Long id);
    Optional<TransfertDto> findByReference(String reference);
}
