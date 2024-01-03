package com.casamancaise.services;

import com.casamancaise.dto.ClientDto;

import java.util.List;

public interface ClientService {
    ClientDto createClient(ClientDto clientDto);

    ClientDto getClientById(Long id);

    List<ClientDto> getAllClients();

    ClientDto updateClient(Long id, ClientDto clientDto);

    void deleteClient(Long id);

    boolean clientExistsWithCanalDistrib(Long clientId, Integer canalDistribId);
}
