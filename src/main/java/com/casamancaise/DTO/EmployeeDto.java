package com.casamancaise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String nomComplet;
    private String localite;
    private String telephone;
    private Integer fonctionId; // Référence à l'ID de la fonction de l'employé
    private String fonction; // Pour l'affichage, si nécessaire

}
