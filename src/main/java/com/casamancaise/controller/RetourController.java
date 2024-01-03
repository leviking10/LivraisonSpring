package com.casamancaise.controller;

import com.casamancaise.dto.RetourDto;
import com.casamancaise.services.RetourService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

}
