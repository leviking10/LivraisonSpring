package com.casamancaise.controller;

import com.casamancaise.dto.ClientDto;
import com.casamancaise.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto createClient(@RequestBody @Valid ClientDto clientDto) {
        return clientService.createClient(clientDto);
    }

    @GetMapping("/{id}")
    public ClientDto getClientById(@PathVariable Long id) {

        return clientService.getClientById(id);
    }

    @GetMapping
    public List<ClientDto> getAllClients() {

        return clientService.getAllClients();
    }

    @PutMapping("/{id}")
    public ClientDto updateClient(@PathVariable Long id, @RequestBody @Valid ClientDto clientDto) {
        return clientService.updateClient(id, clientDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}
