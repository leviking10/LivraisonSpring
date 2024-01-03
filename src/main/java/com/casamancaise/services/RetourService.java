package com.casamancaise.services;

import com.casamancaise.dto.RetourDto;

import java.util.List;
import java.util.Optional;

public interface RetourService {
    RetourDto saveRetour(RetourDto retourDto);

    RetourDto getRetourById(Long id);

    void deleteRetour(Long id);

    List<RetourDto> getAllRetours();

    RetourDto updateRetour(Long id, RetourDto retourDto);

    Optional<RetourDto> findByReference(String reference);
}
