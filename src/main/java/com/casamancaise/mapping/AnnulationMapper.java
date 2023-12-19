package com.casamancaise.mapping;

import com.casamancaise.dto.AnnulationDto;
import com.casamancaise.entities.Annulation;
import com.casamancaise.entities.ReceptionStock;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AnnulationMapper extends EntityMapper<AnnulationDto,Annulation>{
    AnnulationDto toDto(Annulation annulation);
    Annulation toEntity(AnnulationDto annulationDto);

    default Annulation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Annulation annulation = new Annulation ();
        annulation.setId(id);
        return annulation;
    }

}