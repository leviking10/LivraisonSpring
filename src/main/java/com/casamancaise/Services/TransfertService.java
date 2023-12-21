package com.casamancaise.services;
import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.dto.TransfertDto;
import com.casamancaise.entities.EtatTransfert;
import com.casamancaise.entities.TypeDestinataire;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransfertService {
    TransfertDto saveTransfert(TransfertDto transfertDto);
    TransfertDto getTransfertById(Long id);
    List<TransfertDto> getAllTransferts();
    TransfertDto recevoirTransfert(Long id, LocalDate dateLivraison);
    TransfertDto updateTransfertDestinataire(Long id, TypeDestinataire nouveauTypeDestinataire, Integer nouveauDestinataireId);
    TransfertDto updateTransfertStatus(Long id, EtatTransfert etat);
    TransfertDto annulerTransfert(String reference);
    Optional<TransfertDto> findByReference(String reference);
}
