package com.casamancaise.mapping;

import com.casamancaise.DTO.CanalDistribDto;
import com.casamancaise.Entities.CanalDistrib;
import com.casamancaise.Entities.Client;
import com.casamancaise.Entities.Entrepot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CanalDistribMapper {
    CanalDistribDto canalDistribToCanalDistribDTO(CanalDistrib canalDistrib);
    CanalDistrib canalDistribDTOToCanalDistrib(CanalDistribDto canalDistribDTO);
    default CanalDistrib fromIdToCanaldistrib(Integer id) {
        if (id == null) {
            return null;
        }
        CanalDistrib canaldistrib = new CanalDistrib();
        canaldistrib.setIdCanal(id);
        return canaldistrib;
    }
}
