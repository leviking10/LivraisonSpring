package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DotationDto implements Serializable {
    private Long id;
    private String destinataire;
    private Long articleId; // L'ID de l'article au lieu de l'objet Article
    private LocalDate dateDotation;
    private Integer quantite;
    private String motif;
}