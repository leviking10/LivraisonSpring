package com.casamancaise.controller;

import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.services.ReceptionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/receptions")
public class ReceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ReceptionController.class);
    private final ReceptionService receptionService;

    @Autowired
    public ReceptionController(ReceptionService receptionService) {

        this.receptionService = receptionService;
    }

    @PostMapping
    public ResponseEntity<ReceptionStockDto> createReception(@Valid @RequestBody ReceptionStockDto receptionStockDto) {
        logger.info("ReceptionStockDto received: {}", receptionStockDto);
        ReceptionStockDto savedReception = receptionService.saveReception(receptionStockDto);
        return ResponseEntity.ok(savedReception);
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
