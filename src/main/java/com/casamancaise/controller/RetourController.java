package com.casamancaise.controller;

import com.casamancaise.dto.RetourDto;
import com.casamancaise.services.RetourService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/retours")
public class RetourController {

    private final RetourService retourService;

    @Autowired
    public RetourController(RetourService retourService) {
        this.retourService = retourService;
    }

    @PostMapping
    public ResponseEntity<RetourDto> createRetour(@Valid @RequestBody RetourDto retourDto) {
        log.info("RetourDto received: {}", retourDto);
        RetourDto savedRetour = retourService.saveRetour(retourDto);
        return ResponseEntity.ok(savedRetour);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RetourDto> getRetourById(@PathVariable Long id) {
        RetourDto retourDto = retourService.getRetourById(id);
        return ResponseEntity.ok(retourDto);
    }

    @GetMapping
    public ResponseEntity<List<RetourDto>> getAllRetours() {
        List<RetourDto> retours = retourService.getAllRetours();
        return ResponseEntity.ok(retours);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetour(@PathVariable Long id) {
        retourService.deleteRetour(id);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/annuler/{id}")
    public ResponseEntity<String> annulerTransfert(@PathVariable Long id, @RequestBody String raison) {
        if (!isValidRaison(raison)) {
            return ResponseEntity.badRequest().body("Raison d'annulation invalide.");
        }
        try {
            retourService.annulerRetour(id, raison);
            return ResponseEntity.ok().body("Ce retour a étè annulée avec succès.");
        } catch (RuntimeException e) {
            // Gérer les exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    private boolean isValidRaison(String raison) {
        // Vérifiez que la raison n'est pas trop longue et ne contient que des caractères autorisés
        return raison.length() <= 100 && raison.matches("[\\p{Alnum} .,;!'éèêëàâäôöûüçÉÈÊËÀÂÄÔÖÛÜÇ\"]+");
    }
}
