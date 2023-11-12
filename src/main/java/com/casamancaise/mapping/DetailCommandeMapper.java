package com.casamancaise.mapping;

import com.casamancaise.DTO.DetailCommandeDto;
import com.casamancaise.Entities.DetailCommande;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetailCommandeMapper {
    DetailCommandeDto detailCommandeToDetailCommandeDto(DetailCommande detailCommande);
    DetailCommande detailCommandeDtoToDetailCommande(DetailCommandeDto detailCommandeDto);
}
