package com.casamancaise.services;


import com.casamancaise.dao.ClientRepository;
import com.casamancaise.dto.ClientDto;
import com.casamancaise.entities.Client;
import com.casamancaise.mapping.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@Transactional
public class ClientServiceImpl implements ClientService{
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDto createClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return clientMapper.toDto(client);
    }

    @Override
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(clientMapper::toDto)
                .toList();
    }
/*
        @Override
        public ClientDto updateClient(Long id, ClientDto clientDto) {
            Client existingClient = clientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
            clientMapper.updateClientFromDto(clientDto, existingClient);
            existingClient = clientRepository.save(existingClient);
            return clientMapper.toDto(existingClient);
        }
*/
@Override
public ClientDto updateClient(Long id, ClientDto clientDto) {
    Client existingClient = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

    // Log avant la mise à jour pour confirmer l'état de l'entité
    log.info("Mise à jour du client avec l'ID: {}, et l'état actuel de l'entité: {}", id, existingClient);

    // Vérifiez que le ClientDto ne contient pas un 'id' différent de celui que vous mettez à jour
    if (clientDto.getId() != null && !clientDto.getId().equals(id)) {
        throw new IllegalArgumentException("L'ID du ClientDto ne correspond pas à l'ID de l'entité à mettre à jour.");
    }

    clientMapper.updateClientFromDto(clientDto, existingClient);

    // Log après la mise à jour pour confirmer les changements
    log.info("État de l'entité Client après le mappage: {}", existingClient);

    existingClient = clientRepository.save(existingClient);

    // Log après l'enregistrement
    log.info("Client mis à jour avec succès avec l'état final de l'entité: {}", existingClient);

    return clientMapper.toDto(existingClient);
}

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

}
