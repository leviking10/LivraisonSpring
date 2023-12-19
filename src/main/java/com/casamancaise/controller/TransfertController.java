package com.casamancaise.controller;
import com.casamancaise.dto.TransfertDto;
import com.casamancaise.exeption.EntityNotFoundException;
import com.casamancaise.services.TransfertService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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