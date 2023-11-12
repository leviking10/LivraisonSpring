package com.casamancaise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeDto {
    private Long idVehi;
    private String matrVehi;
    private String prestataire;
    private String telephone;

}
