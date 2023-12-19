package com.casamancaise.services;

import com.casamancaise.dto.RetourDto;

import java.util.List;

public interface RetourService {
    RetourDto saveRetour(RetourDto retourDto);
    RetourDto getRetourById(Long id);
    void deleteRetour(Long id);
    List<RetourDto> getAllRetours();
    RetourDto updateRetour(Long id, RetourDto retourDto);
}
