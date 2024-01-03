package com.casamancaise.mapping;

import com.casamancaise.dto.VenteDto;
import com.casamancaise.entities.Vente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, EntrepotMapper.class, DetailVenteMapper.class})
public interface VenteMapper extends EntityMapper<VenteDto, Vente> {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "entrepot.idEntre", target = "entrepotId")
    VenteDto toDto(Vente vente);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "entrepotId", target = "entrepot")
    Vente toEntity(VenteDto venteDto);

    default Vente mapToVente(Long id) {
        if (id == null) {
            return null;
        }
        Vente vente = new Vente();
        vente.setId(id);
        return vente;
    }
}