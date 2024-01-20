package com.casamancaise.dto;

import com.casamancaise.enums.EtatTransfert;
import com.casamancaise.enums.TypeDestinataire;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate receptionDate;
    private boolean isDeleted;
    private double poids;
    // Liste des détails de transfert, représentée par des DTOs
    private List<TransferDetailsDto> transferDetails;
}