package com.casamancaise.mapping;

import com.casamancaise.DTO.ClientDto;
import com.casamancaise.Entities.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDto clientToClientDTO(Client client);
    Client clientDTOToClient(ClientDto clientDTO);
}
