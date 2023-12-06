package com.casamancaise.mapping;

import com.casamancaise.dto.TransfertDto;
import com.casamancaise.entities.Transfert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EntrepotMapper.class, VehiculeMapper.class})
public interface TransfertMapper extends EntityMapper<TransfertDto, Transfert> {
    @Mapping(source = "fromEntrepot.idEntre", target = "fromEntrepotId")
    @Mapping(source = "toEntrepot.idEntre", target = "toEntrepotId")
    @Mapping(source = "vehicule.idVehi", target = "vehiculeId")
    TransfertDto toDto(Transfert transfert);

    @Mapping(source = "fromEntrepotId", target = "fromEntrepot")
    @Mapping(source = "toEntrepotId", target = "toEntrepot")
    @Mapping(source = "vehiculeId", target = "vehicule")
    Transfert toEntity(TransfertDto transfertDto);

    default Transfert mapToTransfert(Long id) {
        if (id == null) {
            return null;
        }
        Transfert transfert = new Transfert();
        transfert.setId(id);
        return transfert;
    }
}
