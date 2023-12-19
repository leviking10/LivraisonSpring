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
public class RetourDto implements Serializable {
    private Long id;
    private LocalDate dateRetour;
    private String raison;
    private List<DetailRetourDto> detailsRetours;

    // Getters, Setters, Constructeurs...
}