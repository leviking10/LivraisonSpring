package com.casamancaise.controller;

import com.casamancaise.dto.DotationDto;
import com.casamancaise.services.DotationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dotations")
public class DotationController {
    private static final Logger logger = LoggerFactory.getLogger(DotationController.class);
    private final DotationService dotationService;

    @Autowired
    public DotationController(DotationService dotationService) {
        this.dotationService = dotationService;
    }

    @PostMapping
    public ResponseEntity<DotationDto> createDotation(@Valid @RequestBody DotationDto dotationDto) {
        logger.info("DotationDto received: {}", dotationDto);
        DotationDto savedDotation = dotationService.saveDotation(dotationDto);
        return ResponseEntity.ok(savedDotation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DotationDto> getDotationById(@PathVariable Long id) {
        DotationDto dotationDto = dotationService.getDotationById(id);
        return ResponseEntity.ok(dotationDto);
    }

    @GetMapping
    public ResponseEntity<List<DotationDto>> getAllDotations() {
        List<DotationDto> dotations = dotationService.getAllDotations();
        return ResponseEntity.ok(dotations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDotation(@PathVariable Long id) {
        dotationService.deleteDotation(id);
        return ResponseEntity.ok().build();
    }

}
