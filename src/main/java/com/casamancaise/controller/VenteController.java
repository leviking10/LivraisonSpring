package com.casamancaise.controller;

import com.casamancaise.dto.VenteDto;
import com.casamancaise.services.VenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventes")
public class VenteController {

    @Autowired
    private VenteService venteService;

    @PostMapping
    public ResponseEntity<VenteDto> createVente(@RequestBody VenteDto venteDto) {
        VenteDto createdVente = venteService.saveVente(venteDto);
        return ResponseEntity.ok(createdVente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenteDto> getVente(@PathVariable Long id) {
        VenteDto vente = venteService.getVenteById(id);
        return ResponseEntity.ok(vente);
    }

    @GetMapping
    public ResponseEntity<List<VenteDto>> getAllVentes() {
        List<VenteDto> ventes = venteService.getAllVentes();
        return ResponseEntity.ok(ventes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VenteDto> deleteVente(@PathVariable Long id) {
        venteService.deleteVente(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<VenteDto> getVenteByReference(@PathVariable String reference) {
        return venteService.findByReference(reference)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
