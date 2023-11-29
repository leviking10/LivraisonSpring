package com.casamancaise.dto;

import com.casamancaise.entities.Mouvement;
import com.casamancaise.entities.TypeMouvement;
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
public class MouvementDto implements Serializable {
    private Long id;
    private Long inventaireId;
    private LocalDate dateMouvement;
    private Integer quantiteChange;
    private String condition;
    private TypeMouvement type;
    private Long receptionstockId;
    private Long transfertId;
    private Long dotationId;
    private Long venteId;
}
