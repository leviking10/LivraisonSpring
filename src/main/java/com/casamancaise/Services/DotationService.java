package com.casamancaise.services;

import com.casamancaise.dto.DotationDto;

import java.util.List;

public interface DotationService {
    DotationDto createDotation(DotationDto dotationDto);
    DotationDto updateDotation(Long id, DotationDto dotationDto);
    DotationDto getDotationById(Long id);
    List<DotationDto> getAllDotations();
    void deleteDotation(Long id);
}