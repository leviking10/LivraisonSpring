package com.casamancaise.controller;

import com.casamancaise.dto.MouvementSortieDto;
import com.casamancaise.entities.MouvementSortie;
import com.casamancaise.services.MvtSortieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mvtSorties")
public class MvtSortieController {
    private static final Logger logger = LoggerFactory.getLogger(MvtSortieController.class);
    private final MvtSortieService mvtSortieService;
    @Autowired
    public MvtSortieController(MvtSortieService mvtSortieService) {
        this.mvtSortieService = mvtSortieService;
    }

    @PostMapping
    public ResponseEntity<MouvementSortieDto> createMvtSortie(@RequestBody MouvementSortieDto mvtSortieDto) {
        MouvementSortieDto savedMvtSortie = mvtSortieService.SaveMvtSortie(mvtSortieDto);
        return ResponseEntity.ok(savedMvtSortie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MouvementSortieDto> getMvtSortieById(@PathVariable Long id) {
        MouvementSortieDto mvtSortieDto = mvtSortieService.getMvtSortieById(id);
        return ResponseEntity.ok(mvtSortieDto);
    }

    @GetMapping
    public ResponseEntity<List<MouvementSortieDto>> getAllMvtSorties() {
        List<MouvementSortieDto> mvtSorties = mvtSortieService.getAllMvtSorties();
        return ResponseEntity.ok(mvtSorties);
    }
    @GetMapping("/reference/{reference}")
    public ResponseEntity<MouvementSortieDto> getMvtSortieByReference(@PathVariable String reference) {
        return mvtSortieService.findByReference(reference)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}