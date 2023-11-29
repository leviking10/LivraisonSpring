package com.casamancaise.dto;
import com.casamancaise.entities.Mouvement;
import lombok.*;
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
    private List<ReceptionDetailDto> receptionDetails;
    private List<MouvementDto> mouvements;
}
