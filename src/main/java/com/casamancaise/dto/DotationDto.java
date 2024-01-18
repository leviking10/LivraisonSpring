package com.casamancaise.dto;

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
public class DotationDto implements Serializable {
    private Long id;
    private String reference;
    private Integer entrepotId;
    private String destinataire;
    private LocalDate dateDotation;
    private String motif;
    private boolean isDeleted;
    private List<DetailsDotationDto> detailsDotation;
}