package com.casamancaise.services;

import com.casamancaise.dto.VehiculeDto;

import java.util.List;

public interface VehiculeService {
    VehiculeDto createVehicule(VehiculeDto entrepotDto);
    VehiculeDto getVehiculeById(Long id);
    List< VehiculeDto> getAllVehicules();
    VehiculeDto updateVehicule(Long id,  VehiculeDto entrepotDto);
    void deleteVehicule(Long id);
}
