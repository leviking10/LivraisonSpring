package com.casamancaise.controller;

import com.casamancaise.dto.TransfertDto;
import com.casamancaise.dto.UpdateTransfertDestinataireDto;
import com.casamancaise.myexeptions.EntityNotFoundException;
import com.casamancaise.services.TransfertService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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



    @PatchMapping("/annuler/{id}")
    public ResponseEntity<String> annulerTransfert(@PathVariable Long id, @RequestBody String raison) {
        if (!isValidRaison(raison)) {
            return ResponseEntity.badRequest().body("Raison d'annulation invalide.");
        }
        try {
            transfertService.annulerTransfert(id, raison);
            return ResponseEntity.ok().body("Ce transfert a étè annulée avec succès.");
        } catch (RuntimeException e) {
            // Gérer les exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible d'annuler ce transfert" +e);
        }
    }
    private boolean isValidRaison(String raison) {
        // Vérifiez que la raison n'est pas trop longue et ne contient que des caractères autorisés
        return raison.length() <= 100 && raison.matches("[\\p{Alnum} .,;!'éèêëàâäôöûüçÉÈÊËÀÂÄÔÖÛÜÇ\"]+");
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
    public ResponseEntity<Object> updateTransfertDestinataire(@PathVariable Long id, @RequestBody UpdateTransfertDestinataireDto updateDto) {
        try {
            TransfertDto updatedTransfert = transfertService.updateTransfertDestinataire(id, updateDto.getNouveauTypeDestinataire(), updateDto.getNouveauDestinataireId());
            return ResponseEntity.ok(updatedTransfert);
        } catch (EntityNotFoundException e) {
            log.error("Erreur lors de la mise à jour du destinataire : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Erreur lors de la mise à jour du destinataire : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}