package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDto {
    private Integer idCom;
    private LocalDateTime dateCommande;
    private String numCom;
    private ClientDto client;
    private BigDecimal poids;
    private String statut;
}
