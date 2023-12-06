package com.casamancaise.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransfertDto {
    private Integer id;
    private Integer fromEntrepotId; // ID de l'entrepôt d'origine
    private Integer toEntrepotId;   // ID de l'entrepôt de destination
    private Date transferDate;       // Date du transfert
    private boolean isReceived;      // Statut de réception
    private Integer vehiculeId;          // ID du véhicule utilisé pour le transfert

    // Vous pouvez également inclure des DTOs pour les détails de transfert si nécessaire
    private List<TransferDetailsDto> transferDetails;

}