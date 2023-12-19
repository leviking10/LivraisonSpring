package com.casamancaise.dto;
import com.casamancaise.entities.TypeMouvement;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MouvementDto implements Serializable {
    private Long id;
    private Long inventaireId;
    private LocalDate dateMouvement;
    private Integer quantiteChange;
    private String condition;
    private TypeMouvement type;
    private String reference;
}
