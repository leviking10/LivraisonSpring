package com.casamancaise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
