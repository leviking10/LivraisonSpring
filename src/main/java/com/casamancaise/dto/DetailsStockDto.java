package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailsStockDto { private Integer id;
    private Integer articleId;  // Référence à l'ID de l'article
    private String nomArticle;  // Optionnel, pour l'affichage
    private Integer entrepotId; // Référence à l'ID de l'entrepôt
    private String nomEntrepot; // Optionnel, pour l'affichage
    private Integer quantite;   // Quantité de l'article dans l'entrepôt
    // Ajoutez d'autres champs pertinents pour le DTO

    // Si vous souhaitez inclure des informations complètes sur l'article ou l'entrepôt,
    // vous pouvez utiliser des instances de leurs DTOs respectifs à la place des ID.
    private ArticleDto article;
    private EntrepotDto entrepot;

    // Constructeurs, getters et setters générés automatiquement par Lombok
}