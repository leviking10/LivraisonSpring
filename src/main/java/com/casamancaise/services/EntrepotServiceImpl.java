package com.casamancaise.services;

import com.casamancaise.dao.EntrepotRepository;
import com.casamancaise.dto.EntrepotDto;
import com.casamancaise.entities.Entrepot;
import com.casamancaise.mapping.EntrepotMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EntrepotServiceImpl implements EntrepotService {

    private final EntrepotRepository entrepotRepository;
    private final EntrepotMapper entrepotMapper;

    @Autowired
    public EntrepotServiceImpl(EntrepotRepository entrepotRepository, EntrepotMapper entrepotMapper) {
        this.entrepotRepository = entrepotRepository;
        this.entrepotMapper = entrepotMapper;
    }

    @Override
    @Transactional
    public EntrepotDto createEntrepot(EntrepotDto entrepotDto) {
        Entrepot entrepot = entrepotMapper.entrepotDTOToEntrepot(entrepotDto);
        entrepot = entrepotRepository.save(entrepot);
        return entrepotMapper.entrepotToEntrepotDTO(entrepot);
    }

    @Override
    @Transactional(readOnly = true)
    public EntrepotDto getEntrepotById(Integer id) {
        Entrepot entrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fonction not found with id: " + id));
        return entrepotMapper.entrepotToEntrepotDTO(entrepot);
    }

    @Override
    @Transactional
    public void deleteEntrepot(Integer id) {
        if (!entrepotRepository.existsById(id)) {
            throw new EntityNotFoundException("fonction not found with id: " + id);
        }
        entrepotRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrepotDto> getAllEntrepots() {
        List<Entrepot> entrepots = entrepotRepository.findAll();
        return entrepots.stream()
                .map(entrepotMapper::entrepotToEntrepotDTO)
                .toList();
    }

    @Override
    @Transactional
    public EntrepotDto updateEntrepot(Integer id, EntrepotDto entrepotDto) {
        Entrepot existingEntrepot = entrepotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fonction not found with id: " + id));
        // Utilisez le mapper pour mettre à jour les champs de l'article existant.
        entrepotMapper.updateEntrepotFromDto(entrepotDto, existingEntrepot);
        // Sauvegardez les modifications
        Entrepot updatedEntrepot = entrepotRepository.save(existingEntrepot);
        // Renvoyez le DTO mis à jour
        return entrepotMapper.entrepotToEntrepotDTO(updatedEntrepot);
    }
}
