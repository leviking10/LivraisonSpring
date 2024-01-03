package com.casamancaise.dto;

import com.casamancaise.enums.Etat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsDotationDto implements Serializable {
    private Long id;
    private Long dotationId; // L'ID de la dotation associée
    private Long articleId; // L'ID de l'article doté
    private Integer quantity; // Quantité dotée
    private Etat etat;
}
