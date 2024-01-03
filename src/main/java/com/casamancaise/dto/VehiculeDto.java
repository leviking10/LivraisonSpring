package com.casamancaise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeDto {
    private Long idVehi;
    private String matrVehi;
    private String prestataire;
    private String telephone;

}
