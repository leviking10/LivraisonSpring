package com.casamancaise.controller;

import com.casamancaise.dto.VehiculeDto;
import com.casamancaise.services.VehiculeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Vehicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @Autowired
    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeDto createVehicule(@RequestBody @Valid VehiculeDto vehiculeDto) {
        return vehiculeService.createVehicule(vehiculeDto);
    }

    @GetMapping("/{id}")
    public VehiculeDto getVehiculeById(@PathVariable Long id) {
        return vehiculeService.getVehiculeById(id);
    }

    @GetMapping
    public List<VehiculeDto> getAllVehicules() {
        return vehiculeService.getAllVehicules();
    }

    @PutMapping("/{id}")
    public VehiculeDto updateVehicule(@PathVariable Long id, @RequestBody @Valid VehiculeDto vehiculeDto) {
        return vehiculeService.updateVehicule(id, vehiculeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicule(@PathVariable Long id) {
        vehiculeService.deleteVehicule(id);
    }
}
