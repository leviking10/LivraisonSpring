package com.casamancaise.dto;

import com.casamancaise.entities.EtatTransfert;
import com.casamancaise.entities.TypeDestinataire;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransfertDto {
    private Long id;
    private Long fromEntrepotId; // ID de l'entrepôt d'origine
    private TypeDestinataire typeDestinataire;
    private Integer destinataireId; // ID du client ou de l'entrepôt de destination
    private LocalDate transferDate;
    private Long vehiculeId; // ID du véhicule utilisé pour le transfert
    private EtatTransfert etat;
    private String reference;
    private LocalDate ReceptionDate;
    // Liste des détails de transfert, représentée par des DTOs
    private List<TransferDetailsDto> transferDetails;        // ID du véhicule utilisé pour le transfert
}