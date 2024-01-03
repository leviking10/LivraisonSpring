package com.casamancaise.mapping;
import com.casamancaise.dto.DotationDto;
import com.casamancaise.entities.Dotation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EntrepotMapper.class, DetailsDotationMapper.class})
public interface DotationMapper extends EntityMapper<DotationDto, Dotation> {
    @Mapping(source = "entrepot.idEntre", target = "entrepotId")
    DotationDto toDto(Dotation entity);

    @Mapping(source = "entrepotId", target = "entrepot")
    Dotation toEntity(DotationDto dotationDto);

    default Dotation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dotation dotation = new Dotation();
        dotation.setId(id);
        return dotation;
    }

    void updateFromDto(DotationDto dto, @MappingTarget Dotation entity);
}