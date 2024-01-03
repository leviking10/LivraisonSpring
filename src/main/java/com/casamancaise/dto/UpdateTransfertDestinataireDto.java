package com.casamancaise.dto;

import com.casamancaise.enums.TypeDestinataire;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransfertDestinataireDto {
    private TypeDestinataire nouveauTypeDestinataire;
    private Integer nouveauDestinataireId;
}
