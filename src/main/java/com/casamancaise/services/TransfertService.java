package com.casamancaise.services;

import com.casamancaise.dto.TransfertDto;
import com.casamancaise.enums.TypeDestinataire;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransfertService {
    TransfertDto saveTransfert(TransfertDto transfertDto);

    TransfertDto getTransfertById(Long id);

    List<TransfertDto> getAllTransferts();

    TransfertDto recevoirTransfert(Long id, LocalDate dateLivraison);

    TransfertDto updateTransfertDestinataire(Long id, TypeDestinataire nouveauTypeDestinataire, Integer nouveauDestinataireId);

    void annulerTransfert(Long id, String raison);

    Optional<TransfertDto> findByReference(String reference);
}
