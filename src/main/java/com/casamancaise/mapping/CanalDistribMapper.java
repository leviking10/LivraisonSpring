package com.casamancaise.mapping;

import com.casamancaise.dto.CanalDistribDto;
import com.casamancaise.entities.CanalDistrib;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CanalDistribMapper extends EntityMapper<CanalDistribDto,CanalDistrib> {
    CanalDistribDto toDto(CanalDistrib entity);
    
    CanalDistrib toEntity(CanalDistribDto dto);

    void updateCanalFromDto(CanalDistribDto dto, @MappingTarget CanalDistrib entity);
    default CanalDistrib fromIdToCanaldistrib(Integer id) {
        if (id == null) {
            return null;
        }
        CanalDistrib canaldistrib = new CanalDistrib();
        canaldistrib.setIdCanal(id);
        return canaldistrib;
    }
}
