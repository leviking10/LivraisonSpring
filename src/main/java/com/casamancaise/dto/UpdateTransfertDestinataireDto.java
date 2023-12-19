package com.casamancaise.dto;
import com.casamancaise.entities.TypeDestinataire;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransfertDestinataireDto {
    private TypeDestinataire nouveauTypeDestinataire;
    private Integer nouveauDestinataireId;
}
