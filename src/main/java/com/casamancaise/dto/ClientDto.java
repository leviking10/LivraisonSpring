package com.casamancaise.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    private String telephone;
    private Integer canalDistribId;
    private Long employeeId;
}
