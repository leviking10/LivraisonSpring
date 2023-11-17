package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetailsDto {
    private Integer id;
    private Integer articleId; // Référence à l'ID de l'article
    private Integer quantite;  // Quantité d'articles transférés
    private Integer transfertId; // Référence à l'ID du transfert
    private String nomArticle;
    private String detailsTransfert;
}
