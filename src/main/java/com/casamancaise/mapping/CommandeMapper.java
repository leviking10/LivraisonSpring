package com.casamancaise.mapping;

import com.casamancaise.DTO.CommandeDto;
import com.casamancaise.Entities.Commande;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommandeMapper {
    CommandeDto commandeToCommandeDTO(Commande commande);
    Commande commandeDTOToCommande(CommandeDto commandeDTO);
}
