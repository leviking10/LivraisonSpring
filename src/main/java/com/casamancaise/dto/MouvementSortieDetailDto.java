package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MouvementSortieDetailDto {
    private Long id;
    private Long articleId;
    private Integer quantite;
    private Long mouvementSortieId;
}
