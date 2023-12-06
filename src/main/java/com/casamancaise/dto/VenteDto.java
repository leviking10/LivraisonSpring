package com.casamancaise.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VenteDto implements Serializable {
    private Long id;
    private Long clientId; // L'ID du client
    private Long entrepotId; // L'ID de l'entrepot
    private LocalDate dateVente;

}