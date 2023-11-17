package com.casamancaise.mapping;

import com.casamancaise.dto.DetailCommandeDto;
import com.casamancaise.entities.DetailCommande;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetailCommandeMapper {
    DetailCommandeDto detailCommandeToDetailCommandeDto(DetailCommande detailCommande);
    DetailCommande detailCommandeDtoToDetailCommande(DetailCommandeDto detailCommandeDto);
}
