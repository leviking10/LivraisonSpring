package com.casamancaise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailCommandeDto {
    private Integer idDetailCommande;
    private Integer quantite;
    private Double prixUnitaire;
    private CommandeDto commande;
    private ArticleDto article;
}
