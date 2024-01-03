package com.casamancaise.services;

import com.casamancaise.dto.VenteDto;

import java.util.List;
import java.util.Optional;

public interface VenteService {
    VenteDto saveVente(VenteDto venteDto);

    VenteDto getVenteById(Long id);

    void deleteVente(Long id);

    List<VenteDto> getAllVentes();

    Optional<VenteDto> findByReference(String reference);
}
