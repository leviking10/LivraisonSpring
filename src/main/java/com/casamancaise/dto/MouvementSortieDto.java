package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MouvementSortieDto {
    private Long id;
    private Integer entrepotId;
    private LocalDate dateSortie;
    private String reference;
    private String motif;
    private List<MouvementSortieDetailDto> detailsSortie;
}
