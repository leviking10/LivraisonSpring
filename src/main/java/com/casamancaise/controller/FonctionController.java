package com.casamancaise.controller;
import com.casamancaise.DTO.FonctionDto;
import com.casamancaise.Services.FonctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/fonctions")
public class FonctionController {
    private final FonctionService fonctionService;
    @Autowired
    public FonctionController(FonctionService fonctionService) {
        this.fonctionService = fonctionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FonctionDto createFonction(@RequestBody @Valid FonctionDto fonctionDto) {
        return fonctionService.createFonction(fonctionDto);
    }

    @GetMapping("/{id}")
    public FonctionDto getFonctionById(@PathVariable Integer id) {
        return fonctionService.getFonctionById(id);
    }

    @GetMapping
    public List<FonctionDto> getAllFonctions() {
        return fonctionService.getAllFonctions();
    }
    @PutMapping("/{id}")
    public FonctionDto updateFonction(@PathVariable Integer id, @RequestBody @Valid FonctionDto fonctionDto) {
        return fonctionService.updateFonction(id, fonctionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFonction(@PathVariable Integer  id) {
        fonctionService.deleteFonction(id);
    }
}
