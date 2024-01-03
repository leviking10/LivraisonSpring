package com.casamancaise.mapping;

import com.casamancaise.dto.MouvementSortieDto;
import com.casamancaise.entities.MouvementSortie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EntrepotMapper.class, MouvementSortieDetailMapper.class})
public interface MouvementSortieMapper extends EntityMapper<MouvementSortieDto, MouvementSortie> {

    @Mapping(source = "entrepot.idEntre", target = "entrepotId")
    MouvementSortieDto toDto(MouvementSortie mouvementSortie);

    @Mapping(source = "entrepotId", target = "entrepot")
    MouvementSortie toEntity(MouvementSortieDto mouvementDto);

    default MouvementSortie fromId(Long id) {
        if (id == null) {
            return null;
        }
        MouvementSortie mouvementSortie = new MouvementSortie();
        mouvementSortie.setId(id);
        return mouvementSortie;
    }
}
