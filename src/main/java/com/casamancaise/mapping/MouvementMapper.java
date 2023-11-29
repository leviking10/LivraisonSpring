package com.casamancaise.mapping;

import com.casamancaise.dto.MouvementDto;
import com.casamancaise.entities.Mouvement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring",uses={InventaireMapper.class,ReceptionStockMapper.class})
public interface MouvementMapper extends EntityMapper<MouvementDto, Mouvement> {
    @Override
    @Mapping(source = "inventaire.id", target = "inventaireId")
    @Mapping(source = "receptionStock.id", target = "receptionstockId")
    /*@Mapping(source = "transfert.id", target = "transfertId")
    @Mapping(source = "dotation.id", target = "dotationId")
    @Mapping(source = "vente.id", target = "venteId")*/
    MouvementDto toDto(Mouvement mouvement);

    @Override
    @Mapping(target = "inventaire.id", source = "inventaireId")
    @Mapping(target = "receptionStock.id", source = "receptionstockId")
   /* @Mapping(target = "transfert", expression = "java(dto.getTransfertId() == null ? null : new com.casamancaise.entities.Transfert(dto.getTransfertId()))")
    @Mapping(target = "dotation", expression = "java(dto.getDotationId() == null ? null : new com.casamancaise.entities.Dotation(dto.getDotationId()))")
    @Mapping(target = "vente", expression = "java(dto.getVenteId() == null ? null : new com.casamancaise.entities.Vente(dto.getVenteId()))")*/
    Mouvement toEntity(MouvementDto dto);
}
