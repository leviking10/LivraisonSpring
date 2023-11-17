package com.casamancaise.dto;

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
    private Long fonctionId;
}
