package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailCommandeDto {
    private Integer idDetailCommande;
    private Integer quantite;
    private Double prixUnitaire;
    private CommandeDto commande;
    private ArticleDto article;
}
