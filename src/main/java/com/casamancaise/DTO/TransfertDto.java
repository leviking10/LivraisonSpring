package com.casamancaise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransfertDto {
    private Integer id;
    private Integer fromEntrepotId; // ID de l'entrepôt d'origine
    private Integer toEntrepotId;   // ID de l'entrepôt de destination
    private Date transferDate;       // Date du transfert
    private boolean isReceived;      // Statut de réception
    private Integer idVehi;          // ID du véhicule utilisé pour le transfert

    // Vous pouvez également inclure des DTOs pour les détails de transfert si nécessaire
    private List<TransferDetailsDto> transferDetails;

    // Des champs supplémentaires pour les informations nécessaires, tels que les noms des entrepôts
     private String fromEntrepot;
    private String toEntrepot;
    // Des informations sur le véhicule pourraient également être incluses
     private String vehicule;
}