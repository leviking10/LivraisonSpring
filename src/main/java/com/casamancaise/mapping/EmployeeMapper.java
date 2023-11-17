package com.casamancaise.mapping;

import com.casamancaise.dto.EmployeeDto;
import com.casamancaise.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",uses ={FonctionMapper.class})
public interface EmployeeMapper extends EntityMapper<EmployeeDto,Employee>{
    @Mapping(source = "fonction.idFonc", target = "fonctionId")
    EmployeeDto toDto(Employee employee);
    @Mapping(source = "fonctionId", target = "fonction")
    Employee toEntity(EmployeeDto employeeDto);
    @Mapping(target = "id", ignore = true)
    void updateEmployeeFromDto(EmployeeDto dto, @MappingTarget Employee entity);
    default Employee FromID(Long id){
        if(id == null){
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
