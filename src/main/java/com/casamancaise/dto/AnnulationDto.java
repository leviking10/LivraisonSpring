package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnulationDto {
    private Long id;
    private String ref;
    private String refOperation;
    private LocalDate dateAnnulation;
    private String raison;
}
