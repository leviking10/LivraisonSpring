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
public class InventaireDto implements Serializable {
    private Long id;
    private Long entrepotId;
    private Long articleId;
    private Integer quantiteConforme;
    private Integer quantiteNonConforme;
}
