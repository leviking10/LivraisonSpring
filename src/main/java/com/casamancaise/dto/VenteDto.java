package com.casamancaise.dto;

import com.casamancaise.enums.StatutVente;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VenteDto implements Serializable {
    private Long id;
    @Column(unique = true, nullable = false)
    private String reference;
    private Long clientId; // L'ID du client
    private Long entrepotId; // L'ID de l'entrepot
    private LocalDate dateVente;
    private boolean isDeleted;
    private LocalDate datelivraison;
    private StatutVente statut;
    private double poids;
    private Long vehiculeId;
    private List<DetailVenteDto> detailVentes;
}