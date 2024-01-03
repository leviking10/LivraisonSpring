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
public class ReceptionStockDto implements Serializable {
    private Long id;
    private Integer entrepotId;
    private LocalDate dateReception;
    private String quart;
    private String reference;
    private boolean estAnnulee;
    private List<ReceptionDetailDto> receptionDetails;
}
