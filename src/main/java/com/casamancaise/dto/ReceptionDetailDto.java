package com.casamancaise.dto;
import com.casamancaise.entities.Etat;
import lombok.*;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionDetailDto implements Serializable {
    private Long id;
    private Long idreceptionStock; // On utilise seulement l'ID pour le stock de réception
    private Long idarticle;
    private Integer quantity;

}
