package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetailsDto {
    private Long id;
    private Long articleId; // ID de l'article transféré
    private Integer quantite; // Quantité de l'article transféré
    private Integer bonus; // Bonus éventuel pour cet article
    private Long transfertId; // ID du transfert associé
}
