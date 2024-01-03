package com.casamancaise.services;

import com.casamancaise.dao.VehiculeRepository;
import com.casamancaise.dto.VehiculeDto;
import com.casamancaise.entities.Vehicule;
import com.casamancaise.mapping.VehiculeMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculeServiceImpl implements VehiculeService {
    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;

    @Autowired
    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeMapper = vehiculeMapper;
    }

    @Override
    @Transactional
    public VehiculeDto createVehicule(VehiculeDto vehiculeDto) {
        Vehicule vehicule = vehiculeMapper.vehiculeDTOToVehicule(vehiculeDto);
        vehicule = vehiculeRepository.save(vehicule);
        return vehiculeMapper.vehiculeToVehiculeDTO(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDto getVehiculeById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicule not found with id: " + id));
        return vehiculeMapper.vehiculeToVehiculeDTO(vehicule);
    }

    @Override
    @Transactional
    public void deleteVehicule(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new EntityNotFoundException("vehicule not found with id: " + id);
        }
        vehiculeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDto> getAllVehicules() {
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        return vehicules.stream()
                .map(vehiculeMapper::vehiculeToVehiculeDTO)
                .toList();
    }

    @Override
    @Transactional
    public VehiculeDto updateVehicule(Long id, VehiculeDto vehiculeDto) {
        Vehicule existingVehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicule not found with id: " + id));
        // Utilisez le mapper pour mettre à jour les champs de l'article existant.
        vehiculeMapper.updateVehiculeFromDto(vehiculeDto, existingVehicule);
        // Sauvegardez les modifications
        Vehicule updatedVehicule = vehiculeRepository.save(existingVehicule);
        // Renvoyez le DTO mis à jour
        return vehiculeMapper.vehiculeToVehiculeDTO(updatedVehicule);
    }
}
