package com.casamancaise.dto;

import lombok.*;

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
