package com.casamancaise.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanalDistribDto {
    private Integer idCanal;
    private String canal;
    private List<ClientDto> clients;
}
