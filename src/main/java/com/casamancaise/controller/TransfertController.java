package com.casamancaise.controller;
import com.casamancaise.dto.TransfertDto;
import com.casamancaise.dto.UpdateTransfertDestinataireDto;
import com.casamancaise.entities.EtatTransfert;
import com.casamancaise.exeption.EntityNotFoundException;
import com.casamancaise.services.TransfertService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/transferts")
public class TransfertController {
    private static final Logger logger = LoggerFactory.getLogger(TransfertController.class);
    private final TransfertService transfertService;

    @Autowired
    public TransfertController(TransfertService transfertService) {
        this.transfertService = transfertService;
    }

    @PostMapping
    public ResponseEntity<TransfertDto> createTransfert(@Valid @RequestBody TransfertDto transfertDto) {
        logger.info("TransfertDto received: {}", transfertDto);
        TransfertDto savedTransfert = transfertService.saveTransfert(transfertDto);
        return ResponseEntity.ok(savedTransfert);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransfertDto> getTransfertById(@PathVariable Long id) {
        TransfertDto transfertDto = transfertService.getTransfertById(id);
        return ResponseEntity.ok(transfertDto);
    }

    @GetMapping
    public ResponseEntity<List<TransfertDto>> getAllTransferts() {
        List<TransfertDto> transferts = transfertService.getAllTransferts();
        return ResponseEntity.ok(transferts);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<TransfertDto> updateTransfertStatus(@PathVariable Long id, @RequestBody EtatTransfert etat) {
        TransfertDto updatedTransfert = transfertService.updateTransfertStatus(id, etat);
        return ResponseEntity.ok(updatedTransfert);
    }
    @PatchMapping("/{id}/recevoir")
    public ResponseEntity<TransfertDto> recevoirTransfert(@PathVariable Long id, @RequestBody LocalDate dateLivraison) {
        try {
            TransfertDto updatedTransfert = transfertService.recevoirTransfert(id, dateLivraison);
            return ResponseEntity.ok(updatedTransfert);
        } catch (RuntimeException e) {
            // Gérer les exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PatchMapping("/{id}/destinataire")
    public ResponseEntity<TransfertDto> updateTransfertDestinataire(@PathVariable Long id, @RequestBody UpdateTransfertDestinataireDto updateDto) {
        try {
            TransfertDto updatedTransfert = transfertService.updateTransfertDestinataire(id, updateDto.getNouveauTypeDestinataire(), updateDto.getNouveauDestinataireId());
            return ResponseEntity.ok(updatedTransfert);
        } catch (RuntimeException e) {
            // Gérer les exceptions (par exemple, transfert non trouvé, mise à jour non autorisée, etc.)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfert(@PathVariable Long id) {
        transfertService.deleteTransfert(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}