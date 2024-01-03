package com.casamancaise.controller;

import com.casamancaise.dto.EntrepotDto;
import com.casamancaise.services.EntrepotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Entrepots")
public class EntrepotController {

    private final EntrepotService entrepotService;

    @Autowired
    public EntrepotController(EntrepotService entrepotService) {
        this.entrepotService = entrepotService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntrepotDto createEntrepot(@RequestBody @Valid EntrepotDto entrepotDto) {
        return entrepotService.createEntrepot(entrepotDto);
    }

    @GetMapping("/{id}")
    public EntrepotDto getEntrepotById(@PathVariable Integer id) {
        return entrepotService.getEntrepotById(id);
    }

    @GetMapping
    public List<EntrepotDto> getAllEntrepots() {
        return entrepotService.getAllEntrepots();
    }

    @PutMapping("/{id}")
    public EntrepotDto updateEntrepot(@PathVariable Integer id, @RequestBody @Valid EntrepotDto entrepotDto) {
        return entrepotService.updateEntrepot(id, entrepotDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntrepot(@PathVariable Integer id) {
        entrepotService.deleteEntrepot(id);
    }
}
