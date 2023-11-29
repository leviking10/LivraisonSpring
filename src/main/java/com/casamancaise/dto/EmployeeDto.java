package com.casamancaise.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String nomComplet;
    private String localite;
    private String telephone;
    private Long fonctionId;
}
