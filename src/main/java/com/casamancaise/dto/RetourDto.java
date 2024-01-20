package com.casamancaise.dto;

import com.casamancaise.enums.TypeRetour;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetourDto implements Serializable {
    private Long id;
    private String reference;
    private TypeRetour typeRetour;
    private Long entrepotId; // ID de l'entrepôt
    private Long operationId;
    private LocalDate dateRetour;
    private String raison;
    private Long vehiculeId;
    private boolean isDeleted;
    private List<DetailRetourDto> detailsRetours;

}