package com.casamancaise.services;

import com.casamancaise.dto.VenteDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VenteService {
    VenteDto saveVente(VenteDto venteDto);

    VenteDto getVenteById(Long id);

    void deleteVente(Long id);

    List<VenteDto> getAllVentes();

    VenteDto updateVente(Long venteId, Long nouveauClientId);

    VenteDto livrerProduit(Long venteId, LocalDate dateLivraison);

    void annulerVente(Long id, String raison);

    Optional<VenteDto> findByReference(String reference);
}
