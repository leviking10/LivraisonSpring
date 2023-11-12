package com.casamancaise.mapping;

import com.casamancaise.DTO.EmployeeDto;
import com.casamancaise.Entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "fonction.idFonc", source = "employeeDto.fonctionId")
    Employee toEntity(EmployeeDto employeeDto);

    // Vous pouvez ignorer le champ `fonction` lors du mapping vers l'entité car il sert uniquement à l'affichage.
    @Mapping(target = "fonction", ignore = true)
    EmployeeDto toDto(Employee employee);
}
