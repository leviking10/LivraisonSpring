package com.casamancaise.mapping;
import com.casamancaise.dto.ClientDto;
import com.casamancaise.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, CanalDistribMapper.class})
public interface ClientMapper extends EntityMapper<ClientDto, Client> {
    @Mapping(source = "responsableZone.id", target = "employeeId")
    @Mapping(source = "canalDistrib.idCanal", target = "canalDistribId")
    ClientDto toDto(Client entity);
    @Mapping(source = "employeeId", target = "responsableZone")
    @Mapping(source = "canalDistribId", target = "canalDistrib")
    Client toEntity(ClientDto dto);
    void updateClientFromDto(ClientDto dto, @MappingTarget Client entity);
    default Client fromId(Long id){
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}