package com.casamancaise.mapping;

import com.casamancaise.dto.MouvementDto;
import com.casamancaise.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring",uses={InventaireMapper.class, ReceptionStockMapper.class, TransfertMapper.class, DotationMapper.class, VenteMapper.class})
public interface MouvementMapper extends EntityMapper<MouvementDto, Mouvement> {
    @Override
    @Mapping(source = "inventaire.id", target = "inventaireId")
    @Mapping(source = "receptionStockMv.id", target = "receptionstockId")
    @Mapping(source = "transfert.id", target = "transfertId")
    @Mapping(source = "dotation.id", target = "dotationId")
    @Mapping(source = "vente.id", target = "venteId")
    MouvementDto toDto(Mouvement mouvement);

    @Mapping(source = "inventaireId", target = "inventaire")
    @Mapping(source = "receptionstockId", target = "receptionStockMv")
    @Mapping(source = "transfertId", target = "transfert")
    @Mapping(source = "dotationId", target = "dotation")
    @Mapping(source = "venteId", target = "vente")
    @Override
    Mouvement toEntity(MouvementDto dto);
}
