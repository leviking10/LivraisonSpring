package com.casamancaise.dto;
import com.casamancaise.entities.Etat;
import lombok.*;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionDetailDto implements Serializable {
    private Long id;
    private Long receptionStockId; // On utilise seulement l'ID pour le stock de réception
    private Long articleId;
    private Integer quantity;
    private Etat etat;
    private ArticleDto article;
}
