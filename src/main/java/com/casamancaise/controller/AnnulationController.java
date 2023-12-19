package com.casamancaise.controller;

import com.casamancaise.dto.AnnulationDto;
import com.casamancaise.services.AnnulationService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annulations")
public class AnnulationController {

    @Autowired
    private AnnulationService annulationService;

    @PostMapping
    public ResponseEntity<AnnulationDto> createAnnulation(@RequestBody AnnulationDto annulationDto) {
        AnnulationDto savedAnnulation = annulationService.createAnnulation(annulationDto);
        return ResponseEntity.ok(savedAnnulation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnulationDto> getAnnulationById(@PathVariable Long id) {
        AnnulationDto annulationDto = annulationService.getAnnulationById(id);
        return ResponseEntity.ok(annulationDto);
    }

    @GetMapping
    public ResponseEntity<List<AnnulationDto>> getAllAnnulations() {
        List<AnnulationDto> annulations = annulationService.getAllAnnulations();
        return ResponseEntity.ok(annulations);
    }
}