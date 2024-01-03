package com.casamancaise.controller;

import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.services.ReceptionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptions")
@Slf4j
public class ReceptionController {

    private final ReceptionService receptionService;

    @Autowired
    public ReceptionController(ReceptionService receptionService) {

        this.receptionService = receptionService;
    }

    @PostMapping
    public ResponseEntity<ReceptionStockDto> createReception(@Valid @RequestBody ReceptionStockDto receptionStockDto) {
        log.info("ReceptionStockDto received: {}", receptionStockDto);
        ReceptionStockDto savedReception = receptionService.saveReception(receptionStockDto);
        return ResponseEntity.ok(savedReception);
    }

    @PostMapping("/annuler/{id}")
    public ResponseEntity<ReceptionStockDto> annulerReception(@PathVariable Long id, @RequestBody String raison) {
        receptionService.annulerReception(id, raison);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceptionStockDto> getReceptionById(@PathVariable Long id) {
        ReceptionStockDto receptionStockDto = receptionService.getReceptionById(id);
        return ResponseEntity.ok(receptionStockDto);
    }

    @GetMapping
    public ResponseEntity<List<ReceptionStockDto>> getAllReceptions() {
        List<ReceptionStockDto> receptions = receptionService.getAllReceptions();
        return ResponseEntity.ok(receptions);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReception(@PathVariable Long id) {
        receptionService.deleteReception(id);
        return ResponseEntity.ok().build();
    }
}
