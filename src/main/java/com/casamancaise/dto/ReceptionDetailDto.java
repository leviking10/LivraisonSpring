package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionDetailDto implements Serializable {
    private Long id;
    private Long idreceptionStock; // On utilise seulement l'ID pour le stock de réception
    private Long idarticle;
    private Integer quantity;

}
