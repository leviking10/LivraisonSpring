    package com.casamancaise.mapping;

    import com.casamancaise.dto.EntrepotDto;
    import com.casamancaise.entities.Entrepot;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    import org.mapstruct.MappingTarget;

    @Mapper(componentModel = "spring")
    public interface EntrepotMapper {
        EntrepotDto entrepotToEntrepotDTO(Entrepot entrepot);
        Entrepot entrepotDTOToEntrepot(EntrepotDto entrepotDTO);
        default Entrepot fromIdToEntrepot(Integer id) {
            if (id == null) {
                return null;
            }
            Entrepot entrepot = new Entrepot();
            entrepot.setIdEntre(id);
            return entrepot;
        }
        @Mapping(target = " idEntre", ignore = true)
        void updateEntrepotFromDto(EntrepotDto dto, @MappingTarget Entrepot entity);

    }
