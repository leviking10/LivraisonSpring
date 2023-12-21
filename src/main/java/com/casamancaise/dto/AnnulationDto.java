package com.casamancaise.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnulationDto {
    private Long id;
    private String ref;
    private String refReception;
    private LocalDate dateAnnulation;
    private String raison;
}
