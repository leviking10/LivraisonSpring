package com.casamancaise.mapping;

import com.casamancaise.dto.CommandeDto;
import com.casamancaise.entities.Commande;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandeMapper {
    CommandeDto commandeToCommandeDTO(Commande commande);
    Commande commandeDTOToCommande(CommandeDto commandeDTO);
}
