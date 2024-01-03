package com.casamancaise.controller;

import com.casamancaise.dto.MouvementDto;
import com.casamancaise.services.MouvementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/mouvements")
public class MouvementController {

    private final MouvementService mouvementService;

    @Autowired
    public MouvementController(MouvementService mouvementService) {
        this.mouvementService = mouvementService;
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<MouvementDto>> getMovementsByArticle(@PathVariable Long articleId) {
        List<MouvementDto> mouvements = mouvementService.getMovementsByArticle(articleId);
        return ResponseEntity.ok(mouvements);
    }

    @GetMapping("/entrepot/{entrepotId}")
    public ResponseEntity<List<MouvementDto>> getMovementsByEntrepot(@PathVariable Integer entrepotId) {
        List<MouvementDto> mouvements = mouvementService.getMovementsByEntrepot(entrepotId);
        return ResponseEntity.ok(mouvements);
    }

    @GetMapping("/date")
    public ResponseEntity<List<MouvementDto>> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<MouvementDto> mouvements = mouvementService.getMovementsByDateRange(startDate, endDate);
        return ResponseEntity.ok(mouvements);
    }


}
