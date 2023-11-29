package com.casamancaise.controller;

import com.casamancaise.dto.CanalDistribDto;
import com.casamancaise.services.CanalDistribService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canals")
public class CanalDistribController {
    private final CanalDistribService canalDistribService;

    @Autowired
    public CanalDistribController(CanalDistribService canalDistribService) {
        this.canalDistribService = canalDistribService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CanalDistribDto createCanal(@RequestBody @Valid CanalDistribDto canalDistribDto) {
        return canalDistribService.createCanal(canalDistribDto);
    }

    @GetMapping("/{id}")
    public CanalDistribDto getCanalById(@PathVariable Integer id) {
        return canalDistribService.getCanalById(id);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<CanalDistribDto> getAllCanals() {
        return canalDistribService.getAllCanals();
    }

    @PutMapping("/{id}")
    public CanalDistribDto updateCanal(@PathVariable Integer id, @RequestBody @Valid CanalDistribDto canalDistribDto) {
        return canalDistribService.updateCanal(id, canalDistribDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCanal(@PathVariable Integer id) {
        canalDistribService.deleteCanal(id);
    }
}