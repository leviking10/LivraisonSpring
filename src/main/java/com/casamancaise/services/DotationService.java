package com.casamancaise.services;

import com.casamancaise.dto.DotationDto;

import java.util.List;
import java.util.Optional;

public interface DotationService {
    DotationDto saveDotation(DotationDto dotationDto);

    DotationDto getDotationById(Long id);

    void deleteDotation(Long id);

    List<DotationDto> getAllDotations();

    Optional<DotationDto> findByReference(String reference);
}