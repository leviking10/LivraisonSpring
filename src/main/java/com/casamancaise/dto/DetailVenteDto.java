package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailVenteDto implements Serializable {
    private Long id;
    private Long venteId; // L'ID de la vente associée
    private Long articleId; // L'ID de l'article vendu
    private Integer quantity; // Quantité vendue
    private Integer bonus;
}
